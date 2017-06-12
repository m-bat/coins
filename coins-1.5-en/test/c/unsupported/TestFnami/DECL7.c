int printf(char *s, ...);

struct s1 {
  char a[1];
  struct s2 {
    char a[2];
    struct s3 {
      char a[3];
      struct s4 {
        char a[4];
        struct s5 {
          char a[5];
          struct s6 {
            char a[6];
            struct s7 {
              char a[7];
              struct s8 {
                char a[8];
                struct s9 {
                  char a[9];
                  struct sa {
                    char a[10];
                    struct sb {
                      char a[11];
                      struct sc {
                        char a[12];
                        struct sd {
                          char a[13];
                          struct se {
                            char a[14];
                            struct sf {
                              char a[15];
                              int x;
                            } x;
                          } x;
                        } x;
                      } x;
                    } x;
                  } x;
                } x;
              } x;
            } x;
          } x;
        } x;
      } x;
    } x;
  } x;
} x={
  1,
  1,2,
  1,2,3,
  1,2,3,4,
  1,2,3,4,5,
  1,2,3,4,5,6,
  1,2,3,4,5,6,7,
  1,2,3,4,5,6,7,8,
  1,2,3,4,5,6,7,8,9,
  1,2,3,4,5,6,7,8,9,10,
  1,2,3,4,5,6,7,8,9,10,11,
  1,2,3,4,5,6,7,8,9,10,11,12,
  1,2,3,4,5,6,7,8,9,10,11,12,13,
  1,2,3,4,5,6,7,8,9,10,11,12,13,14,
  1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,
  123
};

int main() {
  printf("%d %d %d\n",(int)sizeof(struct s1),(int)sizeof(struct s6),(int)sizeof(struct sb));
  printf("%d\n",   (int)sizeof x);
  printf("%d %d\n",(int)sizeof x.x.x.x.x.x,                    x.x.x.x.x.a[3]);
  printf("%d %d\n",(int)sizeof x.x.x.x.x.x.x.x.x.x.x,          x.x.x.x.x.x.x.x.x.x.a[6]);
  printf("%d %d\n",(int)sizeof x.x.x.x.x.x.x.x.x.x.x.x.x.x.x.x,x.x.x.x.x.x.x.x.x.x.x.x.x.x.x.a[9]);
  printf("%d\n",               x.x.x.x.x.x.x.x.x.x.x.x.x.x.x.x);
  return 0;
}
