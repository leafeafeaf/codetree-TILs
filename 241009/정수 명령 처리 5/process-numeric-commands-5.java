import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        ArrayList<Integer> arr = new ArrayList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine());

        for(int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine());

            String command = st.nextToken();
            if(command.equals("push_back")){
                arr.add(Integer.parseInt(st.nextToken()));
            }else if(command.equals("get")){
                System.out.println(arr.get(Integer.parseInt(st.nextToken())-1));
            }else if(command.equals("size")){
                System.out.println(arr.size());
            }else if(command.equals("pop_back")){
                arr.remove(arr.size()-1);
            }

        }
    }
}