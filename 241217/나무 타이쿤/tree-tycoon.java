import java.io.*;
import java.util.*;

public class Main {
	static int size;
	static int years;
	static int[] dy = new int[] { 0, -1, -1, -1, 0, 1, 1, 1 };
	static int[] dx = new int[] { 1, 1, 0, -1, -1, -1, 0, 1 };
	static ArrayList<int[]> supplies = new ArrayList<>(); // y, x
	static ArrayList<int[]> commands = new ArrayList<>(); // dir power
	static int[][] trees;
	static int[][] times;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		solution();
	}

	private static void solution() {
		int sum = 0;
		/*
		 * - for years - 영양제 리스트를 모두 돌면서(방향과 이동거리에 따라 위치 업데이트) - 영양제 리스트에서 위치에 있는 모든 위치를
		 * 기준으로 대각선 4방향 확인(1,3,5,7)해서 1+트리가 있는 개수를 상향 - 영양제 리스트 모두 삭제 - 배열을 모두 순회하면서
		 * 2이상인 친구 2깍고 해당 좌표 영양제 리스트에 추가
		 */

		for (int year = 1; year <= years; year++) {
//			 * - 영양제 리스트를 모두 돌면서(방향과 이동거리에 따라 위치 업데이트) 
//			업데이트된 위치 times 갱신
			//trees++
			int[] command = commands.get(year-1);
			int dir = command[0];
			int power = command[1];
			
			for (int i=0; i<supplies.size();i++) {
				int[] supply = supplies.get(i);
				int y = supply[0];
				int x = supply[1];
				
				y = nomalize(y+dy[dir]*power);
				x = nomalize(x+dx[dir]*power);
				supply[0] = y;
				supply[1] = x;
				
				trees[y][x]++;
				times[y][x]=year;
			}

//			 * - 영양제 리스트에서 위치에 있는 모든 위치를
//			 * 기준으로 대각선 4방향 확인(1,3,5,7)해서 +트리가 있는 개수를 상향 
			for (int[] supply : supplies) {
				int y = supply[0];
				int x = supply[1];
				
				for(int i=1;i<8;i+=2) {
					int hy = y+dy[i];
					int hx = x+dx[i];
					
					if(check(hy,hx) && trees[hy][hx]>0) {
						trees[y][x]++;
					}
				}
			}
			supplies.clear();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (times[i][j] != year && trees[i][j] >= 2) {
						trees[i][j] -= 2;
						supplies.add(new int[] { i, j });
					}
				}
			}
		}

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				sum += trees[i][j];
			}
		}

		System.out.println(sum);
	}

	private static int nomalize(int i) {
		//TODO
		if(i<0) return i+size;
		return i%size;
	}

	private static void init(BufferedReader br) throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		size = Integer.parseInt(st.nextToken());
		years = Integer.parseInt(st.nextToken());

		trees = new int[size][size];
		times = new int[size][size];

		for (int i = 0; i < size; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < size; j++) {
				trees[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		for (int i = 0; i < years; i++) {
			st = new StringTokenizer(br.readLine());
			int dir = Integer.parseInt(st.nextToken())-1;
			int power = Integer.parseInt(st.nextToken());

			commands.add(new int[] { dir, power });
		}

		supplies.add(new int[] { size-1, 0 });
		supplies.add(new int[] { size-1, 1 });
		supplies.add(new int[] { size-2, 0 });
		supplies.add(new int[] { size-2, 1 });
	}

	private static boolean check(int y, int x) {
		return y >= 0 && y < size && x >= 0 && x < size;
	}
}
/*
 * 격자 n*n 끝과끝 연결, 반대편으로 돌아감
 * 
 * 서로 다른 높이의 리브로수
 * 
 * 영양제 1 증가 씨앗이 있으면 1을 만들어냄
 * 
 * 좌하단 4*4칸에 영양제
 * 
 * 영양제는 이동함 1.방향 2.칸 수
 * 
 * 방향배열 dy dx 8방향 (1~8) 우 우상 상 상좌 좌 좌하 하 우하
 * 
 * 1년 1. 영양제 이동 2. 영양제 투입, 영양제는 사라짐 3. 대각선 인접에 1,3,5,7 나무가 있는만큼 +1, 이때 벗어나면 세지
 * 않음 (check함수) 4. 해당 나무를 제외한 2보다 큰 나무 -2 하고 해당 위치에 영양제 투입
 * 
 * - 영양제 이동했을때 반대편으로 이동하는 로직 필요
 * 
 * 입력 격자크기, 년수 n*n 나무 수 각 연도별 이동 수칙 (년수만큼)
 * 
 * 필요한 변수) 격자크기 size(15) 년수 years(100) 방향배열 dy= new int[]{0,1,1,1,0,-1,-1,-1};
 * dx = new int[]{1,1,0,-1,-1,-1,0,1}; 영양제 클래스 {y, x} 영양제 리스트, (삭제할때 뒤에서부터) 이동
 * 방향,크기 리스트 트리 배열 (2차원) 영양제 맞은 시간 저장 배열
 * 
 * 필요한 함수) init(버퍼리더) 데이터 입력받기 - 이동방향은 -1해줘야함
 * 
 * solution() 문제해결 - 트리배열의 모든합 sum을 리턴한다. - sum - for years
 * 
 * - 영양제 리스트를 모두 돌면서(방향과 이동거리에 따라 위치 업데이트) - 영양제 리스트에서 위치에 있는 모든 위치를 기준으로 대각선
 * 4방향 확인(1,3,5,7)해서 1+트리가 있는 개수를 상향 - 영양제 리스트 모두 삭제 (moveSupply) - 배열을 모두 순회하면서
 * 2이상인 친구 2깍고 해당 좌표 영양제 리스트에 추가 (영양제 맞은 위치는 제외)
 * 
 * - 모두 끝난후 sum을 다 더해서 출력
 * 
 * check(y,x) 배열 내부에 있는지 확인
 * 
 * 
 */