import java.util.*;
import java.io.*;

import static java.nio.file.Files.move;

public class Main {

    static class Player {
        int id, y, x, dir, power, weapon;

        public Player(int id, int y, int x, int dir, int power, int weapon) {
            this.id = id;
            this.y = y;
            this.x = x;
            this.dir = dir;
            this.power = power;
            this.weapon = weapon;
        }
    }

    static int[] dy = new int[]{-1, 0, 1, 0};
    static int[] dx = new int[]{0, 1, 0, -1};
    static ArrayList<Player> playerList = new ArrayList<>();
    static int[][] players;
    static int[] scores;
    static PriorityQueue<Integer>[][] weapons;
    static int size;
    static int playerNum;
    static int round;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        solution();
    }

    private static void solution() {
        StringBuilder sb = new StringBuilder();

        for (int t = 0; t < round; t++) {
//            System.out.println("=================");
            for (Player p : playerList) {
                //            이동 (무기 확인)
//            겹치는지 확인(플레이어 위치 배열)
//            겹치면{
//                승부
//                승자 점수 높이기(score) 배열)
//                패자 무기 떨구기
//                승자 무기 줍기
//                패자 이동(무기 확인)
//            }
//
//                System.out.print("("+p.y+" : "+p.x+") "+p.dir+" ");
                players[p.y][p.x] = 0;
                int hy = p.y + dy[p.dir];
                int hx = p.x + dx[p.dir];
                //끝이면
                if (!check(hy, hx)) {
                    p.dir = (p.dir + 2) % 4;
                    hy = p.y + dy[p.dir];
                    hx = p.x + dx[p.dir];
                }

                //만약 없으면
                if(players[hy][hx] == 0){
                    getWeapon(p,hy,hx);
                    players[hy][hx] = p.id;
                    p.y = hy;
                    p.x = hx;
                }else{
                    //승부
                    Player enemy = playerList.get(players[hy][hx]-1);
                    Player win,lose;
                    //내가 이김
                    if(fight(p,enemy)){
                        win = p;
                        lose = enemy;
                    }
                    //상대가 이김
                    else{
                        win = enemy;
                        lose = p;
                    }

                    players[hy][hx] = win.id;
                    win.y = hy;
                    win.x = hx;

                    //승자 점수 높이기
                    scores[win.id-1] += win.power+win.weapon - lose.power- lose.weapon;

                    //패자 무기 떨구기
                    if(lose.weapon != 0){
                        weapons[hy][hx].add(lose.weapon);
                        lose.weapon = 0;
                    }
                    //승자 무기 줍기
                    getWeapon(win,hy,hx);

                    for(int i=0;i<4;i++){
                        int dir = (lose.dir+i)%4;
                        int ny = hy+dy[dir];
                        int nx = hx+dx[dir];

                        if(check(ny,nx) && players[ny][nx] ==0){
                            players[ny][nx] = lose.id;
                            lose.y=ny;
                            lose.x=nx;

                            getWeapon(lose,ny,nx);
                            lose.dir = dir;
                            break;
                        }
                    }


                }
//                System.out.println("("+p.y+" : "+p.x+") "+p.dir+" ");
            }
        }

        for (int i = 0; i < playerNum; i++) {
            sb.append(scores[i]).append(" ");
        }
        System.out.println(sb);
    }
    static boolean fight(Player p, Player enemy){
        int pNum = p.power+p.weapon;
        int eNum = enemy.power+enemy.weapon;

        if(pNum>eNum) return true;
        else if(pNum<eNum) return false;

        return p.power>enemy.power;
    }

    static void getWeapon(Player p, int y, int x){
        if(p.weapon != 0){
            weapons[y][x].add(p.weapon);
            p.weapon = 0;
        }

        if(!weapons[y][x].isEmpty()){
            p.weapon = weapons[y][x].poll();
        }
    }

    static boolean check(int y, int x) {
        return y >= 0 && y < size && x >= 0 && x < size;
    }

    private static void init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        size = Integer.parseInt(st.nextToken());
        playerNum = Integer.parseInt(st.nextToken());
        round = Integer.parseInt(st.nextToken());

        players = new int[size][size];
        scores = new int[playerNum];
        weapons = new PriorityQueue[size][size];

        for (int i = 0; i < size; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < size; j++) {
                weapons[i][j] = new PriorityQueue<>(Collections.reverseOrder());
                int weapon = Integer.parseInt(st.nextToken());
                if (weapon != 0) {
                    weapons[i][j].add(weapon);
                }
            }
        }

        for (int i = 0; i < playerNum; i++) {
            st = new StringTokenizer(br.readLine());

            int y = Integer.parseInt(st.nextToken())-1;
            int x = Integer.parseInt(st.nextToken())-1;
            int dir = Integer.parseInt(st.nextToken());
            int power = Integer.parseInt(st.nextToken());

            playerList.add(new Player(i + 1, y, x, dir, power, 0));
            players[y][x] = i+1;
        }

    }
}

/*
n*n 격자
무기 or 플레이어 가 각칸

플레이어는 초기 능력치(겹치지 않음)

무기(공격력)
플레이어
-번호
-방향


한턴
1.첫번째 플레이어부터 방향으로 이동(끝으로 나아갈 경우 방향을 바꾸고 이동)
2.총을 주움 (가장 높은 공격력을 가진 총, 나머지는 해당 격자에 둠)
3. 이동한 방향에 플레이어가 있으면 싸움, 높은 쪽이 승리하고 합의 차이만큼 포인트 획득
만약 점수가 같으면 기본 점수가 높은 놈이 승리
이긴놈은 가만히
지면 (총내려놓고, 한칸 이동) 만약 끝이라면 90%방향을 바꿔서 플레이어가 없는 곳으로 이동

필요한 함수
init(BufferedReader br) 데이터를 입력받는다
    size, 사람수, 라운드
    총 정보
    플레이어정보(x+1,y+1, 방향, 능력치)

solution() : 최종적으로 플레이어포인트배열을 출력
    for(라운드){
        for(플레이어리스트){
            이동 (무기 확인)
            겹치는지 확인(플레이어 위치 배열)
            겹치면{
                승부
                승자 점수 높이기(score) 배열)
                패자 무기 떨구기
                승자 무기 줍기
                패자 이동(무기 확인)
            }

        }
    }

move(id) :
check(y, x) : 배열 범위를 넘어가지는지 확인


*/