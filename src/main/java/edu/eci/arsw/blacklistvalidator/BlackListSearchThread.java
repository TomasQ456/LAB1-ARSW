package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

/**
 * Hilo que busca un host en un segmento [startIndex, endIndex) de las listas negras.
 *
 * @author Tomas Quiceno Ostos, Deisy Guzman
 */
public class BlackListSearchThread extends Thread {

    private final String ipAddress;
    private final int startIndex;
    private final int endIndex;
    private final HostBlacklistsDataSourceFacade facade;

    private final List<Integer> blackListOccurrences;
    private int checkedListsCount;

    public BlackListSearchThread(String ipAddress, int startIndex, int endIndex, HostBlacklistsDataSourceFacade facade) {
        this.ipAddress = ipAddress;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.facade = facade;
        this.blackListOccurrences = new LinkedList<>();
        this.checkedListsCount = 0;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            checkedListsCount++;
            if (facade.isInBlackListServer(i, ipAddress)) {
                blackListOccurrences.add(i);
            }
        }
    }

    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }

    public int getOccurrencesCount() {
        return blackListOccurrences.size();
    }

    public int getCheckedListsCount() {
        return checkedListsCount;
    }
}
