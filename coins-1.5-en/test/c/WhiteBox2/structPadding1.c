/* structPadding1.c  Kitamura mail 030827 */

struct {
  struct {
    int j;
    char d;
  } st;
  int j;  /* padding is required */
} st = {
  2, 3, 4
};

int main()
{
  printf("%d\n", st.st.j);
  printf("%d\n", st.st.d);
  printf("%d\n", st.j);
  return 0;
}

