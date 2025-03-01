import java.util.*;
import java.io.*;


public class Main {
    static int rSize, cSize;
    static int k;
    static int[][] forest;
    static int[] dy = {-1,0,1,0};
    static int[] dx = {0,1,0,-1};

    static int ny, nx,ndir;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        solution(br);
    }

    static boolean[][] visited;

    private static void solution(BufferedReader br) throws IOException{
        int sum = 0;
        StringTokenizer st;

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            ny = 1;
            nx = Integer.parseInt(st.nextToken())-1;
            ndir = Integer.parseInt(st.nextToken());

            if(moveGolem()){
                visited = new boolean[rSize+3][cSize];
                int bottom = dfs(ny,nx)-2;
//                System.out.println("\n답 "+bottom);

                sum += bottom;
            }else{
//                System.out.println("초기화");
                clearForest();
            }
        }

        System.out.println(sum);
    }


    private static int dfs(int y, int x) {
        visited[y][x] = true;
        int max = y;
        if(forest[y][x] == 1){
            for (int i = 0; i < 4; i++) {
                int hy = y+dy[i];
                int hx = x+dx[i];

                if(check(hy,hx) && forest[hy][hx] == 3 && !visited[hy][hx]){
                    max = Math.max(max, dfs(hy,hx));
                }
            }
        }else{
            for (int i = 0; i < 4; i++) {
                int hy = y+dy[i];
                int hx = x+dx[i];

                if(check(hy,hx) && forest[hy][hx] != 0 && !visited[hy][hx]){
                    max = Math.max(max, dfs(hy,hx));
                }
            }
        }
        return max;
    }

    static int[][] draw = {{-1,0}, {0,1},{1,0},{0,-1},{0,0}};
    private static boolean moveGolem() {

        //남쪽으로 갈수있는지
        //서쪽
        //동쪽
        while(true){
            if(canMoveSouth()){
//                System.out.print("남쪽");
            } else if (canMoveWest()) {
//                System.out.print("서쪽");
                nx -= 1;
                ndir = (ndir-1 +4) %4;
            } else if (canMoveEast()) {
//                System.out.print("동쪽");
                nx += 1;
                ndir = (ndir+1)%4;
            } else{
                break;
            }
            ny += 1;
//            System.out.print("("+ny+","+nx+")"+ndir+" ");
        }
        //갈수없으면
        // 현재 ny 가 1~2면 return false;
        if(ny <= 3) return false;
        //forest에 현재 ny nx에 따라 값을 새긴다
        for(int i=0;i<5;i++){
            forest[ny+draw[i][0]][nx+draw[i][1]] = (i==ndir) ? 2 : 1;
        }
        forest[ny][nx] = 3;
        return true;
    }
/*
1. 남쪽 한칸 (중앙 기준 (1,-1) , (2,0) , (1,1) 가 비어있어야함
2. 서쪽 회전하면서 내려감 (-1,-1), (0,-2), (1,-1)가 비어있고 (1,-2), (2,-1)가 비어있어야함 (출구가 시계 반대방향)
3. 동쪽 회전 (0,2) (-1,1) (1,1) (1,2) , (2,1) 출구가 시계 방향
 */
    static int[][] southCheck= {{1,-1},{2,0},{1,1}};
    static int[][] westCheck= {{-1,-1},{0,-2},{1,-1},{1,-2},{2,-1}};
    static int[][] eastCheck= {{0,2},{-1,1},{1,1},{1,2},{2,1}};

    private static boolean canMoveEast() {
        //ny와 nx를
        for (int[] next : eastCheck) {
            int hy = ny+next[0];
            int hx = nx+next[1];

            if(!check(hy,hx) || forest[hy][hx] != 0){
                return false;
            }
        }
        return true;
    }

    private static boolean canMoveWest() {
        //ny와 nx를
        for (int[] next : westCheck) {
            int hy = ny+next[0];
            int hx = nx+next[1];

            if(!check(hy,hx) || forest[hy][hx] != 0){
                return false;
            }
        }
        return true;
    }
    //남쪽 한칸 (중앙 기준 (1,-1) , (2,0) , (1,1) 가 비어있어야함
    private static boolean canMoveSouth() {
        //ny와 nx를
        for (int[] next : southCheck) {
            int hy = ny+next[0];
            int hx = nx+next[1];

            if(!check(hy,hx) || forest[hy][hx] != 0){
                return false;
            }
        }
        return true;
    }

    static boolean check(int y, int x){
        return y>=0 && y<rSize+3 && x>=0 && x<cSize;
    }
    private static void clearForest() {
        for (int i = 0; i < rSize + 3; i++) {
            for (int j = 0; j < cSize; j++) {
                forest[i][j] = 0;
            }
        }
    }
    private static void init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        rSize = Integer.parseInt(st.nextToken());
        cSize = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        forest = new int[rSize+3][cSize];
    }
}

/*
3초
필요한 함수나 변수
3초

출발하는 열 , 방향 (0,1,2,3) (북 동 남 서)

init() 값을 입력받기
solution 문제 해결
clearForest 숲초기화
check 배열 벗어나는지 확인
dfs() 가장 남쪽으로 이동하는 함수

골렘 class{
    int y, int y
    dir (출구 방향)
}
r+3 *

 */

/*
R * C 마법숲

1~R

위에서 떨어짐?

K명의 정렬이 골렘을 타고 숲 탐색

골렘은 십자 모양 구조_5칸 차지)

십자 4개중 한개가 출구

탑승은 아무대나 하차는 출구를 통해서만 내릴 수 있음

골렘 움직임 우선순위
1. 남쪽 한칸 (중앙 기준 (1,-1) , (2,0) , (1,1) 가 비어있어야함
2. 서쪽 회전하면서 내려감 (-1,-1), (0,-2), (1,-1)가 비어있고 (1,-2), (2,-1)가 비어있어야함 (출구가 시계 반대방향)
3. 동쪽 회전 (0,2) (-1,1) (1,1) (1,2) , (2,1) 출구가 시계 방향


가장 남쪽으로 이동하면 골렘은 멈춤  -> 정령이 골렘을 타고 아래로 내려감 (출구가 다른 곳과 붙어있어야함)

정령이 가장 남쪽으로 가면 해당 위치가 답
이것들을 행 총합을 누적

만약 골렘의 위치가 숲의 위치를 벗어나면 숲 초기화 (점수 계산 X)

*/