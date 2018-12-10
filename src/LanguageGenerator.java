import java.io.*;
import java.util.*;

public class LanguageGenerator {
    HashSet<String> V;
    HashSet<Character> T;

    ArrayList<Production> P;

    HashSet<String> LHS=new HashSet<>();

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
        for (Production s :
                p) {
            LHS.add(s.lhs);
        }
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
            for (String t :
                    LHS) {
                if (start.contains(t)) {
                    ArrayList<String> allRhs = getAllRhs(t);
                    for (String rhs :
                            allRhs) {
                        ret.addAll(generate(start.replace(t, rhs), length));
                    }
                }
            }
            if (isFinal(start))
                ret.add(start);
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

    private boolean isFinal(String s){
        for (String v :
                V) {
            if (s.contains(v))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        HashSet<String> V = new HashSet<>();
        HashSet<Character> T = new HashSet<>();
        V.add("S");V.add("A");V.add("B");V.add("C");V.add("D");
        T.add('a');T.add('b');T.add('c');T.add('d');T.add('#');
        ArrayList<Production> productions = new ArrayList<>();
        productions.add(new Production("S", "ABCD"));
        productions.add(new Production("S", "abc#"));
        productions.add(new Production("A", "aaA"));
        productions.add(new Production("AB", "aabbB"));
        productions.add(new Production("BC", "bbccC"));
        productions.add(new Production("cC", "cccC"));
        productions.add(new Production("CD", "ccd#"));
        productions.add(new Production("CD", "d#"));
        productions.add(new Production("CD", "#d"));
        LanguageGenerator LG = new LanguageGenerator(V, T, productions);
        ArrayList<String> ret = LG.generate("S", 30);
//        System.out.println(ret.toString());
        for (String s: ret) {
            System.out.println(s);
        }
    }
}
