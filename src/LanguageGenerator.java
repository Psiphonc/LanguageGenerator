import java.io.*;
import java.util.*;

public class LanguageGenerator {
    HashSet<String> V;
    HashSet<Character> T;

    ArrayList<Production> P;

    static class Production {
        String lhs;
        String rhs;

        public Production(String key, String value) {
            this.lhs = key;
            this.rhs = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Production that = (Production) o;
            return Objects.equals(lhs, that.lhs) &&
                    Objects.equals(rhs, that.rhs);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lhs, rhs);
        }

        @Override
        public String toString() {
            return "Production{" +
                    "lhs='" + lhs + '\'' +
                    ", rhs='" + rhs + '\'' +
                    '}';
        }
    }

    public LanguageGenerator(HashSet<String> v, HashSet<Character> t, ArrayList<Production> p) {
        V = v;
        T = t;
        P = p;
    }

    public LanguageGenerator(FileInputStream fis) {
        readTable(fis);
    }

    private void readTable(FileInputStream fis) {
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String V_t = null;
        String T_t = null;
        String S_t = null;
        P = new ArrayList<Production>();
        try {
            V_t = br.readLine().replaceAll(" ", "");
            T_t = br.readLine().replaceAll(" ", "");
            S_t = br.readLine().replaceAll(" ", "");
            String p_line;
            while ((p_line = br.readLine()) != null) {
                p_line.trim();
                String[] pair = p_line.split("->");
                P.add(new Production(pair[0], pair[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        V = new HashSet<String>(Arrays.asList(V_t.split(",")));

        T = new HashSet<>();
        String[] T_split = T_t.split(",");
        for (String s :
                T_split) {
            T.add(s.charAt(0));
        }

    }

    public ArrayList<String> generate(String start, int length) {
        ArrayList<String> ret = new ArrayList<>();
        if (start.length() <= length) {
            boolean final_flag = true;
            for (String t :
                    V) {
                if (start.contains(t)) {
                    ArrayList<String> allRhs = getAllRhs(t);
                    for (String rhs :
                            allRhs) {
                        ret.addAll(generate(start.replace(t, rhs), length));
                        final_flag = false;
                    }
                }
                if (final_flag)
                    ret.add(start);
            }
        }
        return ret;
    }

    private ArrayList<String> getAllRhs(String s) {
        ArrayList<String> ret = new ArrayList<>();
        for (Production p :
                P) {
            if (p.lhs.equals(s)) {
                ret.add(p.rhs);
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        HashSet<String> V = new HashSet<>();
        HashSet<Character> T = new HashSet<>();
        V.add("A");
        T.add('0');
        T.add('1');
        ArrayList<Production> productions = new ArrayList<>();
        productions.add(new Production("A", "1A0"));
        productions.add(new Production("A", "0A1"));
        productions.add(new Production("A", "0"));
        productions.add(new Production("A", "1"));
        LanguageGenerator LG = new LanguageGenerator(V, T, productions);
        ArrayList<String> ret = LG.generate("A", 5);
        System.out.println(ret.toString());
    }
}
