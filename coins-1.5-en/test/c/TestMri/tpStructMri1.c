/* tpStructMri1.c */

int main()
{
        struct tag{
                int a;
        };
        struct tag  z;
        z.a= 1;

        printf("z.a = %d\n",z.a); /* SF030509 */
}
