import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.*;

// 2회차 기준 구현 (LMS 수정안 반영). STAGE만 바꿔 1~4단계로 쓴다
public class Application {
    static final int STAGE = 2;

    public static void main(String[] args) {
        System.out.println("구입금액을 입력해 주세요.");
        int money = Integer.parseInt(Console.readLine().trim());
        System.out.println();
        int count = money / 1000;
        int manual = 0;
        List<List<Integer>> lottos = new ArrayList<>();
        if (STAGE >= 4) {
            System.out.println("수동으로 구매할 로또 수를 입력해 주세요.");
            manual = Integer.parseInt(Console.readLine().trim());
            System.out.println();
            System.out.println("수동으로 구매할 번호를 입력해 주세요.");
            for (int i = 0; i < manual; i++) lottos.add(sorted(parse(Console.readLine())));
            System.out.println();
        }
        for (int i = manual; i < count; i++) {
            lottos.add(sorted(Randoms.pickUniqueNumbersInRange(1, 45, 6)));
        }
        if (STAGE >= 4) System.out.println("수동으로 " + manual + "개, 자동으로 " + (count - manual) + "개를 구매했습니다.");
        else System.out.println(count + "개를 구매했습니다.");
        lottos.forEach(l -> System.out.println(l));
        if (STAGE == 1) return;
        System.out.println();
        System.out.println("지난 주 당첨 번호를 입력해 주세요.");
        List<Integer> win = parse(Console.readLine());
        System.out.println();
        int bonus = -1;
        if (STAGE >= 3) {
            System.out.println("보너스 볼을 입력해 주세요.");
            bonus = Integer.parseInt(Console.readLine().trim());
            System.out.println();
        }
        int[] c = new int[7]; int second = 0;
        for (List<Integer> l : lottos) {
            int m = (int) l.stream().filter(win::contains).count();
            if (STAGE >= 3 && m == 5 && l.contains(bonus)) second++; else c[m]++;
        }
        long prize = c[3] * 5000L + c[4] * 50000L + c[5] * 1500000L + second * 30000000L + c[6] * 2000000000L;
        System.out.println("당첨 통계");
        System.out.println("---------");
        System.out.println("3개 일치 (" + won(5000) + "원) - " + c[3] + "개");
        System.out.println("4개 일치 (" + won(50000) + "원) - " + c[4] + "개");
        System.out.println("5개 일치 (" + won(1500000) + "원) - " + c[5] + "개");
        if (STAGE >= 3) System.out.println("5개 일치, 보너스 볼 일치 (" + won(30000000) + "원) - " + second + "개");
        System.out.println("6개 일치 (" + won(2000000000) + "원) - " + c[6] + "개");
        BigDecimal rate = BigDecimal.valueOf(prize).divide(BigDecimal.valueOf(money), 2, RoundingMode.DOWN);
        System.out.println("총 수익률은 " + rate.toPlainString() + "입니다.(기준이 1이기 때문에 결과적으로 " + (rate.compareTo(BigDecimal.ONE) >= 0 ? "이익" : "손해") + "라는 의미임)");
    }

    static String won(long amount) {
        return String.valueOf(amount);
    }

    static List<Integer> sorted(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>(numbers);
        Collections.sort(result);
        return result;
    }

    static List<Integer> parse(String line) {
        return Arrays.stream(line.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
    }
}
