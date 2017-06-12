/* tpsizeof11.c:  sizeof bug */
/*     Kitamura mail 030925 */

int printf(char *, ...);

struct {
    struct {
        int  i;
    } st2;
} a0[10];

struct st {
    struct {
        int  i;
    } st2;
} a[10];

int main()
{
    printf("%d\n", sizeof(struct st));
    printf("%d\n", sizeof(a0));
    printf("%d\n", sizeof(a));
    return 0;
}
