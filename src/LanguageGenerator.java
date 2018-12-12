import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class LanguageGenerator {
    HashSet<String> V;
    HashSet<Character> T;

    ArrayList<Production> P;

    HashSet<String> LHS = new HashSet<>();

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
            return this.lhs + "->" + this.rhs;
        }
    }

    public LanguageGenerator(HashSet<String> v, HashSet<Character> t, ArrayList<Production> p) {
        V = v;
        T = t;
        P = p;
        initLHS();
    }

    private void initLHS() {
        for (Production s :
                P) {
            LHS.add(s.lhs);
        }
    }

    public LanguageGenerator(FileInputStream fis) {
        readTable(fis);
        initLHS();
    }

    private void readTable(FileInputStream fis) {
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String V_t = null;
        String T_t = null;
        P = new ArrayList<>();
        try {
            V_t = br.readLine().replaceAll(" ", "");
            T_t = br.readLine().replaceAll(" ", "");
            String p_line;
            while ((p_line = br.readLine()) != null) {
                p_line.trim();
                String[] pair = p_line.split("->");
                P.add(new Production(pair[0], pair[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        V = new HashSet<>(Arrays.asList(V_t.split(",")));

        T = new HashSet<>();
        String[] T_split = T_t.split(",");
        for (String s :
                T_split) {
            T.add(s.charAt(0));
        }

    }

    public HashSet<String> generate(String start, int length) {
        HashSet<String> ret = new HashSet<>();
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
            if (isFinal(start)) {
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

    private boolean isFinal(String s) {
        for (String v :
                V) {
            if (s.contains(v))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String ret = "";
        for (String s :
                V) {
            ret += s;
            ret += ",";
        }
        ret = ret.substring(0, ret.length() - 1) + "\n";
        for (char s :
                T) {
            ret += s;
            ret += ",";
        }
        ret = ret.substring(0, ret.length() - 1) + "\n";
        for (Production s :
                P) {
            ret += s.toString();
            ret += "\n";
        }
        return ret;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("./srcdata.txt");
        LanguageGenerator LG = new LanguageGenerator(fis);
        HashSet<String> ret = LG.generate("S", 30);
        System.out.println(LG.toString());
        System.out.println(ret.toString());
    }
}
