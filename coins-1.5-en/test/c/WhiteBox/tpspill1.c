/* tpspill1.c -- Register spill test */

int a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w,
    x, y, z;
int main()
{
  a = (a+(b+(c+(d+(e+(f+(g+(h+(i+(j+(k+(l+(m+(n+(o+(p+(q+(r+(s+
       (t+(u+(v+(w+(x+(y+(z))))))))))))))))))))))))));

  printf("Register spill test\n"); /* SF030509 */

  return 0;
}
