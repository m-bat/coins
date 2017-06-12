   int a;
   int b;
   int printf(char*, ...);

   void ControlTest(int ii) {
     int i;
     if(ii < 3) {
       for(i = 0; i < 2; i++) {
 	a = a * a;
 	b = b * b;
       }
       i = i - 1;
       while(i < 3) {
 	a = a + b;
        i++;
       }
       i = i - 1;
       do {
 	b = a + b;
        i++;
       } while(i < 3);

       if(ii < 2) {
 	a = a + 3;
       } else {
        a = a + 4;
 	b = b + 1;
       }

       switch(ii) {
       case 1:
 	a = a + 1;
 	b = b + 1;
 	a = a + b;
 	break;
       case 2:
 	a = a + 2;
 	b = b + 2;
 	b = a + b;
 	break;
       default: ; /* ##82 */
       }
       ControlTest(ii + 1);
     }
   }

int main()
{
  a = 2;
  b = 3;
  ControlTest(1);
  printf("a=%d b=%d \n", a, b);
  return 0;
}


