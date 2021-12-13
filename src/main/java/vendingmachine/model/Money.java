package vendingmachine.model;

import camp.nextstep.edu.missionutils.Randoms;
import vendingmachine.Coin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Money {
    private static final List<Integer> AMOUNT_LIST = Coin.getAmountList();
    private static final int DEFALUT_CNT = 0;
    private static final int DEFALUT_TOTAL = 0;

    private Map<Coin, Integer> coins = new LinkedHashMap<>();

    public Money(int total) {
        initialMap();
        setRandomCnt(total);
    }

    public Map<Coin, Integer> getChanges(int total) {
        Map<Coin, Integer> changes = new LinkedHashMap<>();

        for (Entry<Coin, Integer> entry : coins.entrySet()) {
            Coin coin = entry.getKey();
            int cnt = getMaxCntByTotal(coin.getAmount(), entry.getValue(), total);
            changes.put(coin, cnt);
            coins.put(coin, coins.get(coin) - cnt);
            total -= cnt * entry.getValue();
            if (total <= 0) {
                break;
            }
        }
        return changes;
    }

    public int getTotalMoney() {
        int total = DEFALUT_TOTAL;
        for (Entry<Coin, Integer> entry : coins.entrySet()) {
            total += entry.getKey().getAmount() * entry.getValue();
        }
        return total;
    }

    private int getMaxCntByTotal(int amount, int cnt, int total) {
        int maxCnt = (int) Math.floor( (double) total / amount);
        return Math.min(maxCnt, cnt);
    }

    private void initialMap() {
        for (Coin value : Coin.values()) {
            coins.put(value, DEFALUT_CNT);
        }
    }

    private void setRandomCnt(int total) {
        int temp = DEFALUT_TOTAL;
        while (temp != total) {
            int random = Randoms.pickNumberInList(AMOUNT_LIST);
            if (random + temp > total) {
                continue;
            }
            Coin coin = Coin.findNameByValue(random);
            if (coin == null) {
                continue;
            }
            coins.put(coin, coins.getOrDefault(coin, DEFALUT_CNT) + 1);
            temp += random;
        }
    }
}