/*
３人の宣教師と３人の人食い人が川を渡ろうとしている。川には２人のりボ−トが
一槽ある。こちら側，向こう側，ボ−トの上のいずれについても人食い人の人数が
宣教師の数より多いと宣教師は食べられてしまう。6人は無事川を渡ることが
できるだろうか?
定数 M,C,Bは，それぞれ宣教師の人数，人食い人の数，ボ−トの定員である。
mh[i],ch[i]が時刻i(i=0,1,2,...)におけるこちら側の宣教師，人食い人の数である。
mb[j],cb[j]は,ボ−トにのる宣教師，人食い人の人数として可能な値の対で(j=0,1,2,...
..np-1)これは main()の最初でセットする。
flag[m][c]はこちら側の宣教師，人食い人の人数の対(m,c)が不可であることを表す
フラグである。偶数時刻（ 船は向こうに行く），奇数時刻（船はこちらに来る）に
応じて違うビットを立てる。全く可能性のない組み合わせだけ main()でセットしておき
あとは同じ状態の再現を防ぐため try()の後半で随時立てたり倒したりする。
*/
#include <stdio.h>
#include <stdlib.h>

#define M  5  /* 宣教師の数 */
#define C  5  /* 人食い人の数 */
#define B  4  /* ボートの定員 */

int np, solution;
unsigned char mb[(B+1)*(B+2)/2], cb[(B+1)*(B+2)/2],
	mh[2*(M+1)*(C+1)], ch[2*(M+1)*(C+1)], flag[M+1][C+1];

void found(int n)  /* Display answer */
{
	int i;
	static char mmm[] = "MMMMMMMMMM", ccc[] = "CCCCCCCCCC";

	printf("solution %d\n", ++solution);
	for (i = 0; i <= n; i++) {
		printf("%4d  %-*.*s %-*.*s  /  %-*.*s %-*.*s\n",
			i, M, mh[i], mmm, C, ch[i], ccc,
			   M, M - mh[i], mmm, C, C - ch[i], ccc);
	}
}

void try(void)  /* 再帰的に試す */
{
	static i = 0;
	int j, m, c;

        if (solution > 2) return;  /* Do not repeat too many times */
	i++;
	for (j = 1; j < np; j++) {
		if (i & 1) {  /* 奇数回目は向こうに行く */
			m = mh[i - 1] - mb[j];  c = ch[i - 1] - cb[j];
		} else {      /* 偶数回目はこちらに来る */
			m = mh[i - 1] + mb[j];  c = ch[i - 1] + cb[j];
		}
		if (m < 0 || c < 0 || m > M || c > C ||
				(flag[m][c] & (1 << (i & 1)))) continue;
		mh[i] = m;  ch[i] = c;
		/* if (m == 0 && c == 0) found(i); */
		if (m == 0 && c == 0) { found(i); break; } /* Do not repeat too many times */
		else {
			flag[m][c] |= 1 << (i & 1);  try();
			flag[m][c] ^= 1 << (i & 1);
		}
	}
	i--;
}

int main()
{
	int m, c;

	np = 0;
	for (m = 0; m <= B; m++) for (c = 0; c <= B - m; c++)
		if (m == 0 || m >= c) {
			mb[np] = m;  cb[np] = c;  np++;
		}
	for (m = 0; m <= M; m++) for (c = 0; c <= C; c++)
		if ((m > 0 && m < c) || (m < M && M - m < C - c))
			flag[m][c] |= 1 | 2;
	mh[0] = M;  ch[0] = C;  flag[M][C] |= 1;
	solution = 0;  try();
	if (solution == 0) printf("No solution was found \n");
	return EXIT_SUCCESS;
}
