/* tpenum1.c:  Enumeration 1 */

int a, b, c;
enum color {red, yellow, green};
enum color c1;

int main()
{
  enum color c2;
  enum {off, on} flag;

  c1 = red;
  c2 = green;
  a  = c1;
  flag = on;

  printf("red,yellow,green = %d,%d,%d\n",red,yellow,green); /* SF030509 */
  printf("off,on = %d,%d\n",off,on); /* SF030509 */
  printf("c1,c2 = %d,%d\n",c1,c2); /* SF030509 */
  printf("a = %d\n",a); /* SF030509 */
  printf("flag = %d\n",flag); /* SF030509 */

  return 0;
}

