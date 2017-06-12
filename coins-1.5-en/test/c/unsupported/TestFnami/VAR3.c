int printf(char *s, ...);

double f(int x) {
  char x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,xa,xb,xc,xd,xe,xf;
  short y0,y1,y2,y3,y4,y5,y6,y7,y8,y9,ya,yb,yc,yd,ye,yf;
  int z0,z1,z2,z3,z4,z5,z6,z7,z8,z9,za,zb,zc,zd,ze,zf;
  long s0,s1,s2,s3,s4,s5,s6,s7,s8,s9,sa,sb,sc,sd,se,sf;
  long long t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,ta,tb,tc,td,te,tf;
  float u0,u1,u2,u3,u4,u5,u6,u7,u8,u9,ua,ub,uc,ud,ue,uf;
  double v0,v1,v2,v3,v4,v5,v6,v7,v8,v9,va,vb,vc,vd,ve,vf;
  long double w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,wa,wb,wc,wd,we;
  x0=x1=x2=x3=x4=x5=x6=x7=x8=x9=xa=xb=xc=xd=xe=xf=0;
  y0=y1=y2=y3=y4=y5=y6=y7=y8=y9=ya=yb=yc=yd=ye=yf=0;
  z0=z1=z2=z3=z4=z5=z6=z7=z8=z9=za=zb=zc=zd=ze=zf=0;
  s0=s1=s2=s3=s4=s5=s6=s7=s8=s9=sa=sb=sc=sd=se=sf=0;
  t0=t1=t2=t3=t4=t5=t6=t7=t8=t9=ta=tb=tc=td=te=tf=0;
  u0=u1=u2=u3=u4=u5=u6=u7=u8=u9=ua=ub=uc=ud=ue=uf=0;
  v0=v1=v2=v3=v4=v5=v6=v7=v8=v9=va=vb=vc=vd=ve=vf=0;
  w0=w1=w2=w3=w4=w5=w6=w7=w8=w9=wa=wb=wc=wd=we=0;
  while(x--) {
    x0+=x1+=x2+=x3+=x4+=x5+=x6+=x7+=x8+=x9+=xa+=xb+=xc+=xd+=xe+=xf+=1;
    y0+=y1+=y2+=y3+=y4+=y5+=y6+=y7+=y8+=y9+=ya+=yb+=yc+=yd+=ye+=yf+=2;
    z0+=z1+=z2+=z3+=z4+=z5+=z6+=z7+=z8+=z9+=za+=zb+=zc+=zd+=ze+=zf+=3;
    s0+=s1+=s2+=s3+=s4+=s5+=s6+=s7+=s8+=s9+=sa+=sb+=sc+=sd+=se+=sf+=4;
    t0+=t1+=t2+=t3+=t4+=t5+=t6+=t7+=t8+=t9+=ta+=tb+=tc+=td+=te+=tf+=5;
    u0+=u1+=u2+=u3+=u4+=u5+=u6+=u7+=u8+=u9+=ua+=ub+=uc+=ud+=ue+=uf+=6;
    v0+=v1+=v2+=v3+=v4+=v5+=v6+=v7+=v8+=v9+=va+=vb+=vc+=vd+=ve+=vf+=7;
    w0+=w1+=w2+=w3+=w4+=w5+=w6+=w7+=w8+=w9+=wa+=wb+=wc+=wd+=we+=8;
  }
  return x0+y0+z0+s0+t0+u0+v0+w0;
}

int main() {
  printf("%g\n",f(2));
  return 0;
}
