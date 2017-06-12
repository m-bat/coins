int printf(char *s, ...);

int main() {
  int i,j,k;
  int f=0;
  for(i=0;i<4;i++) {
    j=1;
    while(j<(1<<i)) {
      k=-15;
      do {
        printf("%d %d %d\n",i,j,k);
        if(i==3 && j==2 && k==1) {
          int i,j;
          int c=0;
          f=1;
          for(i=0;i<10;i+=2) {
            for(j=0;j<10;j++) {
              switch(j) {
              case 0:
                switch(i) {
                case 4:
                  printf("i=%d j=%d\n",i,j);
                  break;
                default:
                  c++;
                }
                break;
              case 1:
                switch(i) {
                case 6:
                  printf("i=%d j=%d\n",i,j);
                default:
                  c++;
                }
                break;
              case 2: continue;
              case 3: continue;
              case 4: continue;
              case 5: continue;
              case 6: continue;
              case 7: continue;
              case 8: continue;
              case 9:
                printf("j=%d\n",j);
                if(i==8) {
  int i,j,k;
  int f=0;
  for(i=0;i<4;i++) {
    j=1;
    while(j<(1<<i)) {
      k=-15;
      do {
        printf("%d %d %d\n",i,j,k);
        if(i==3 && j==2 && k==1) {
          int i,j;
          int c=0;
          f=1;
          for(i=0;i<10;i+=2) {
            for(j=0;j<10;j++) {
              switch(j) {
              case 0:
                switch(i) {
                case 4:
                  printf("i=%d j=%d\n",i,j);
                  break;
                default:
                  c++;
                }
                break;
              case 1:
                switch(i) {
                case 6:
                  printf("i=%d j=%d\n",i,j);
                default:
                  c++;
                }
                break;
              case 2: continue;
              case 3: continue;
              case 4: continue;
              case 5: continue;
              case 6: continue;
              case 7: continue;
              case 8: continue;
              case 9:
                printf("j=%d\n",j);
                break;
              }
            }
          }
          printf("c=%d\n",c);
        } else {
          if(f) printf("bad control");
        }
      } while((k/=-2)!=0);
      if(j==2) break;
      j<<=1;
    }
  }
}
                break;
              }
            }
          }
          printf("c=%d\n",c);
        } else {
          if(f) printf("bad control");
        }
      } while((k/=-2)!=0);
      if(j==2) break;
      j<<=1;
    }
  }
  return 0;
}
