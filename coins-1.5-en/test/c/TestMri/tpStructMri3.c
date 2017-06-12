/* tpStructMri3.c  struct/union assignment */

struct s {
  int x[100];
};

union u {
  int a;
  int b;
  struct s c;
};

int main()
{
  union u v;
  union u v1;
  struct s vv;
  struct s vv1;

  v1.a = 5; /* SF030509 */
  vv1.x[0] = 10; /* SF030509 */

  v = v1;
  vv = vv1;

  printf("v1  = [%d,%d,[%d]]\n",v1.a,v1.b,v1.c.x[0]); /* SF030509 */
  printf("v = [%d,%d,[%d]]\n",v.a,v.b,v.c.x[0]); /* SF030509 */
  printf("vv1 = [%d]\n",vv1.x[0]); /* SF030509 */
  printf("vv = [%d]\n",vv.x[0]); /* SF030509 */

  return 0;
}
