int printf(char *s, ...);

int main() {
 int i;
 i = 1;
 switch ( i ){
    case 1: i = 2;
    case 2: i = 1;
 }
 printf("i = %d\n", i);
 return 0;
}