int printf(char *s, ...);

struct s {
  int x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,xa,xb,xc,xd,xe,xf;
  int y0,y1,y2,y3,y4,y5,y6,y7,y8,y9,ya,yb,yc,yd,ye,yf;
  int z0,z1,z2,z3,z4,z5,z6,z7,z8,z9,za,zb,zc,zd,ze,zf;
  int s0,s1,s2,s3,s4,s5,s6,s7,s8,s9,sa,sb,sc,sd,se,sf;
  int t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,ta,tb,tc,td,te,tf;
  int u0,u1,u2,u3,u4,u5,u6,u7,u8,u9,ua,ub,uc,ud,ue,uf;
  int v0,v1,v2,v3,v4,v5,v6,v7,v8,v9,va,vb,vc,vd,ve,vf;
  int w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,wa,wb,wc,wd,we;
} x;

int main() {
  char *p=(char *)&x;
  x.x7=1; x.y8=2; x.z9=3; x.sa=4; x.tb=5; x.uc=6; x.vd=7; x.we=8;
  printf("%d %d %d %d %d %d %d %d\n",x.x7,x.y8,x.z9,x.sa,x.tb,x.uc,x.vd,x.we);
  printf("%d %d %d %d\n",(char *)&x.t0-p,(char *)&x.u0-p,(char *)&x.w0-p,(char *)&x.we-p);
  return 0;
}
