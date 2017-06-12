int printf(char *s, ...);

int f0=3;
int f1(int x) { return x+4; }
int f2(int p1(int)) { return p1(f0); }
int f3(int p2(int p1(int))) { return p2(f1); }
int f4(int p3(int p2(int p1(int)))) { return p3(f2); }
int f5(int p4(int p3(int p2(int p1(int))))) { return p4(f3); }
int f6(int p5(int p4(int p3(int p2(int p1(int)))))) { return p5(f4); }
int f7(int p6(int p5(int p4(int p3(int p2(int p1(int))))))) { return p6(f5); }
int f8(int p7(int p6(int p5(int p4(int p3(int p2(int p1(int)))))))) { return p7(f6); }
int f9(int p8(int p7(int p6(int p5(int p4(int p3(int p2(int p1(int))))))))) { return p8(f7); }
int fa(int p9(int p8(int p7(int p6(int p5(int p4(int p3(int p2(int p1(int)))))))))) { return p9(f8); }
int fb(int pa(int p9(int p8(int p7(int p6(int p5(int p4(int p3(int p2(int p1(int))))))))))) { return pa(f9); }
int fc(int pb(int pa(int p9(int p8(int p7(int p6(int p5(int p4(int p3(int p2(int p1(int)))))))))))) { return pb(fa); }

int main() {
  printf("%d\n",fc(fb));
  return 0;
}
