int printf(char *s, ...);

int main() {
  int a=1;
  {
    int a=2;
    {
      int a=3;
      {
        int a=4;
        {
          int a=5;
          {
            int a=6;
            {
              int a=7;
              {
                int a=8;
                {
                  int a=9;
                  {
                    int a=10;
                    {
                      int a=11;
                      {
                        int a=12;
                        {
                          int a=13;
                          {
                            int a=14;
                            {
                              int a=15;
                              {
                                int a=16;
                                printf("%d",a);
                              }
                              printf(" %d",a);
                            }
                            printf(" %d",a);
                          }
                          printf(" %d",a);
                        }
                        printf(" %d",a);
                      }
                      printf(" %d",a);
                    }
                    printf(" %d",a);
                  }
                  printf(" %d",a);
                }
                printf(" %d",a);
              }
              printf(" %d",a);
            }
            printf(" %d",a);
          }
          printf(" %d",a);
        }
        printf(" %d",a);
      }
      printf(" %d",a);
    }
    printf(" %d",a);
  }
  printf(" %d",a);
  return 0;
}
