/* tpStructMri2.c */

int main()
{
        struct VVVVV{
                int bb[100];
                struct xx {
                        int b[100];
                        int a[100];
                } x;
        };
        int i, j;
        struct VVVVV  av;
        struct VVVVV  *pp;
        struct VVVVV  aa[10];

        i = 3; /* SF030509 */
        j = 2; /* SF030509 */

        av.bb[i]= 1;
        av.x.b[i]= 2;
        pp = &av;
        pp->bb[i] =3;
        pp->x.b[i] =4;
        pp = pp +1;
        aa[i].bb[j] = 0;
        pp--;

        printf("i,j = %d,%d\n",i,j); /* SF030509 */
        printf("av.bb[i] = %d\n",av.bb[i]); /* SF030509 */
        printf("av.x.b[i] = %d\n",av.x.b[i]); /* SF030509 */
        printf("pp->bb[i] = %d\n",pp->bb[i]); /* SF030509 */
        printf("pp->x.b[i] = %d\n",pp->x.b[i]); /* SF030509 */
        printf("aa[i].bb[j] = %d\n",aa[i].bb[j]); /* SF030509 */
}

