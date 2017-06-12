/*
���ͤ��붵�դȣ��ͤοͿ����ͤ�����Ϥ��Ȥ��Ƥ��롣��ˤϣ��ͤΤ�ܡݥȤ�
���夢�롣������¦��������¦���ܡݥȤξ�Τ�����ˤĤ��Ƥ�Ϳ����ͤοͿ���
�붵�դο����¿�����붵�դϿ��٤��Ƥ��ޤ���6�ͤ�̵������Ϥ뤳�Ȥ�
�Ǥ��������?
��� M,C,B�ϡ����줾���붵�դοͿ����Ϳ����ͤο����ܡݥȤ�����Ǥ��롣
mh[i],ch[i]������i(i=0,1,2,...)�ˤ����뤳����¦���붵�ա��Ϳ����ͤο��Ǥ��롣
mb[j],cb[j]��,�ܡݥȤˤΤ��붵�ա��Ϳ����ͤοͿ��Ȥ��Ʋ�ǽ���ͤ��Ф�(j=0,1,2,...
..np-1)����� main()�κǽ�ǥ��åȤ��롣
flag[m][c]�Ϥ�����¦���붵�ա��Ϳ����ͤοͿ�����(m,c)���ԲĤǤ��뤳�Ȥ�ɽ��
�ե饰�Ǥ��롣��������� ���ϸ������˹Ԥ��ˡ������������Ϥ���������ˤ�
�����ư㤦�ӥåȤ�Ω�Ƥ롣������ǽ���Τʤ��Ȥ߹�碌���� main()�ǥ��åȤ��Ƥ���
���Ȥ�Ʊ�����֤κƸ����ɤ����� try()�θ�Ⱦ�ǿ��Ω�Ƥ����ݤ����ꤹ�롣
*/
#include <stdio.h>
#include <stdlib.h>

#define M  5  /* �붵�դο� */
#define C  5  /* �Ϳ����ͤο� */
#define B  4  /* �ܡ��Ȥ���� */

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

void try(void)  /* �Ƶ�Ū�˻ */
{
	static i = 0;
	int j, m, c;

        if (solution > 2) return;  /* Do not repeat too many times */
	i++;
	for (j = 1; j < np; j++) {
		if (i & 1) {  /* ������ܤϸ������˹Ԥ� */
			m = mh[i - 1] - mb[j];  c = ch[i - 1] - cb[j];
		} else {      /* �������ܤϤ��������� */
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
