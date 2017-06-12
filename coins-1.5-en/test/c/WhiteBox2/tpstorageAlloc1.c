/* tpstorageAlloc1.c:  sizeof and storage allocation bug */
/*     Kitamura mail 030925 */

int printf(char*, ...);

struct ct_data {
    struct {
        int  freq;
    } fca[2];
};

struct ct_data ctbuf[4];

int main()
{
    int i, j, k;
    printf("sizeof(strcut ct_data)\t\t%d\n", sizeof(struct ct_data));
    printf("sizeof(ct_data[0])\t\t%d\n", sizeof(ctbuf[0]));
    printf("sizeof(ct_data[0].fca)\t\t%d\n", sizeof(ctbuf[0].fca));
    printf("sizeof(ct_data[0].fca[0])\t%d\n", sizeof(ctbuf[0].fca[0]));
    printf("sizeof(ctbuf)\t\t\t%d\n", sizeof(ctbuf));
    
    k = 0;
    for (j = 0; j < 4; j++) {
	for (i = 0; i < 2; i++) {
	    ctbuf[j].fca[i].freq = k++;
	}
    }
    for (j = 0; j < 4; j++) {
	for (i = 0; i < 2; i++) {
	    printf("%d ", ctbuf[j].fca[i].freq);
	}
    }

    printf("\n");

    return 0;

/** Results

kitamura@jupiter$ java -classpath classes/ coins.driver.Driver foo.c
kitamura@jupiter$ ./a.out 
sizeof(strcut ct_data)          0
sizeof(ct_data[0])              4
sizeof(ct_data[0].fca)          -1
sizeof(ct_data[0].fca[0])       4
sizeof(ctbuf)                   0
0 2 2 4 4 6 6 7 

itamura@jupiter$ gcc  foo.c
kitamura@jupiter$ ./a.out 
sizeof(strcut ct_data)          8
sizeof(ct_data[0])              8
sizeof(ct_data[0].fca)          8
sizeof(ct_data[0].fca[0])       4
sizeof(ctbuf)                   32
0 1 2 3 4 5 6 7 

**/

}
