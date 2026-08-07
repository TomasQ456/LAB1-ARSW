/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N){

        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        int totalServers=skds.getRegisteredServersCount();

        if (N<1){
            throw new IllegalArgumentException("El numero de hilos debe ser al menos 1");
        }
        if (N>totalServers){
            N=totalServers;
        }

        List<BlackListSearchThread> threads=new LinkedList<>();

        int base=totalServers/N;
        int remainder=totalServers%N;
        int start=0;
        for (int t=0;t<N;t++){
            int size=base+(t<remainder?1:0);
            int end=start+size;
            BlackListSearchThread thread=new BlackListSearchThread(ipaddress, start, end, skds);
            threads.add(thread);
            start=end;
        }

        for (BlackListSearchThread thread:threads){
            thread.start();
        }

        for (BlackListSearchThread thread:threads){
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HostBlackListsValidator.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        }

        int ocurrencesCount=0;
        int checkedListsCount=0;
        for (BlackListSearchThread thread:threads){
            blackListOcurrences.addAll(thread.getBlackListOccurrences());
            ocurrencesCount+=thread.getOccurrencesCount();
            checkedListsCount+=thread.getCheckedListsCount();
        }

        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, totalServers});

        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
