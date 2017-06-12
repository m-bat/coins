int printf(char *s, ...);

struct s1 {
  struct s2 {
    struct s3 {
      struct s4 {
        struct s5 {
          struct s6 {
            struct s7 {
              struct s8 {
                struct s9 {
                  struct sa {
                    struct sb {
                      struct sc {
                        struct sd {
                          struct se {
                            struct sf {
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
} x={ 123 };

int main() {
  printf("%d\n",x.x.x.x.x.x.x.x.x.x.x.x.x.x.x.x);
  return 0;
}
