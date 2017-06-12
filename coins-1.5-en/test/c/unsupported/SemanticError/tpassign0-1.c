/* Assign/tpassign0-1.c: void with return */
void main(){
int x,y;
x = 0;
y = 0;
x = y;
printf("This test will cause 'void with return' error.\n"); /* SF030609 */
return x;
}
