import java.util.*;
import java.io.*;

/*
git commit -m "김성민 / 241024 / [BOJ_⚾_17281] / 588ms"

### 풀이 사항
풀이 일자: 2024.10.24
풀이 시간: 45분
채점 결과: 정답
예상 문제 유형: np, 구현
시간: 588 ms
메모리: 21824 kb

### 풀이방법
1. 데이터를 입력받고 순열을 저장할 배열(0번제외)과 타석 순서를 저장할 리스트를 생성한다.
2. 생성된 순열을 리스트에 넣고 0을 3인덱스(4번 타자)에 삽입한다.
3. 리스트 순서대로 이닝을 진행하여 점수를 구하고 최댓값을 갱신한다.
4. next permutation으로 다음 순열을 구하고 2번으로 돌아간다.

### 느낀점
np에 구현에 미숙한 점이 있다는 걸 알아서 좋았다.
이닝이 끝나면 모든 주자를 초기화해야한다는 걸 잊어서 잠깐 시간이 지연되었다.



특이사항)
주사위 (아래 남 동)을 3개 관리 (위 북 서)는 7에서 각각을 뺀것

처음 시작이 (0,0) dir = 0; {0,1,2,3}우 하 좌 상 으로 구현한다.

현재 아래칸 값이 바닥 칸보다 크다면 dir+= 1 하고 %4를 한다.

hy hx가 0보다 작거나 n보다 크거나 같으면 dir+2 %4를 조져주고 y-dy x-dx로 바꿔준다.

int[] dice = [3] 아래 남 동
dy 우 하 좌 상
dx

함수)
check(y,x,n)
m번 만큼 반복(for)
    방향에 맞춰서 이동
    방향 설정
    점수 계산

 */

public class Main {

    static int[] dy = {0,1,0,-1}; // 우 하 좌 상
    static int[] dx = {1,0,-1,0}; // 우 하 좌 상
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int[][] map = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int[][] score = getScore(map,n);
        int[] dice = {6,2,3};//아래 남 동
        int dir = 0;
        int y = 0;
        int x = 0;

        int result = 0;

        for (int i = 0; i < m; i++) {
            //move
            int hy = y+dy[dir];
            int hx = x+dx[dir];
            //dir set
            if(check(hy,hx,n)){
                y = hy;
                x = hx;
            }else{
                y -= dy[dir];
                x -= dx[dir];

                dir = (dir+2)%4;
            }
            //roll
            roll(dice, dir);
            if(dice[0]>map[y][x]){
                dir= (dir+1)%4;
            } else if (dice[0] < map[y][x]) {
                dir = (dir+3)%4;
            }

            //score
            result += score[y][x];
        }

        sb.append(result);
        System.out.print(sb);
    }

    private static void roll(int[] dice, int dir) {//// 우 하 좌 상
        int tmp;
        if(dir==0){
            //아래 서 위 동
            tmp = 7-dice[0];
            dice[0] = dice[2];
            dice[2] = tmp;
        }else if(dir == 1){
            tmp = 7-dice[0];
            dice[0] = dice[1];
            dice[1] = tmp;
        }else if(dir == 2){
            tmp = 7-dice[2];
            dice[2] = dice[0];
            dice[0] = tmp;
        }else{
            tmp = 7-dice[1];
            dice[1] = dice[0];
            dice[0] = tmp;
        }
    }

    private static int[][] getScore(int[][] map, int n) {
        int[][] arr = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(arr[i][j] != 0) continue;
                bfs(map, arr, n, i, j);
            }
        }

        return arr;
    }

    private static void bfs(int[][] map, int[][] arr, int n, int i, int j) {
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] {i,j});
        int sum = 0;
        int num = map[i][j];

        while(!queue.isEmpty()){
            int[] node = queue.poll();

            int y = node[0];
            int x = node[1];

            visited[y][x] = true;
            sum+= map[y][x];
            for (int k = 0; k < 4; k++) {
                int hy = y+dy[k];
                int hx = x+dx[k];

                if(check(hy,hx,n) && map[hy][hx] == num && !visited[hy][hx] ){
                    queue.add(new int[] {hy,hx});
                }
            }
        }

        queue.add(new int[] {i,j});

        while(!queue.isEmpty()){
            int[] node = queue.poll();

            int y = node[0];
            int x = node[1];

            visited[y][x] = false;
            arr[y][x] = sum;
            for (int k = 0; k < 4; k++) {
                int hy = y+dy[k];
                int hx = x+dx[k];

                if(check(hy,hx,n) && visited[hy][hx] ){
                    queue.add(new int[] {hy,hx});
                }
            }
        }
    }

    public static boolean check(int y, int x, int n){
        return y>=0 && x>=0 && y<n && x<n;
    }
}