 .ident "Coins Compiler version: coins-1.4.4.1 + BackEnd-1.0"
/* JavaCG for target:sparc convention:standard */

	.section ".text"
	.align	4
__sgetc:
	save	%sp,-96,%sp
	ld	[%i0+4],%i1
	sub	%i1,1,%i1
	st	%i1,[%i0+4]
	ld	[%i0+4],%i1
	cmp	%i1,0
	bge	.L4
	nop
.L3:
	mov	%i0,%o0
	call	__srget
	nop
	mov	%o0,%i1
	ba	.L5
	nop
.L4:
	ld	[%i0],%i1
	ldub	[%i1],%i1
	ld	[%i0],%i2
	add	%i2,1,%i2
	st	%i2,[%i0]
.L5:
	ldsh	[%i0+12],%i3
	sethi	%hi(16384),%i2
	or	%i2,%lo(16384),%i2
	and	%i3,%i2,%i2
	cmp	%i2,0
	be	.L18
	nop
.L6:
	cmp	%i1,13
	bne	.L8
	nop
.L7:
	mov	1,%i2
	ba	.L9
	nop
.L8:
	mov	0,%i2
.L9:
	sll	%i2,16,%i2
	srl	%i2,16,%i2
	sll	%i2,16,%i2
	sra	%i2,16,%i2
	cmp	%i2,0
	be	.L18
	nop
.L10:
	ld	[%i0+4],%i2
	sub	%i2,1,%i2
	st	%i2,[%i0+4]
	ld	[%i0+4],%i2
	cmp	%i2,0
	bge	.L12
	nop
.L11:
	mov	%i0,%o0
	call	__srget
	nop
	ba	.L13
	nop
.L12:
	ld	[%i0],%i2
	ldub	[%i2],%o0
	ld	[%i0],%i2
	add	%i2,1,%i2
	st	%i2,[%i0]
.L13:
	cmp	%o0,10
	bne	.L15
	nop
.L14:
	mov	%o0,%i1
	ba	.L18
	nop
.L15:
	mov	%i0,%o1
	call	ungetc
	nop
.L18:
	mov	%i1,%i0
	ret
	restore

	.align	1
string.9:
	.byte	105
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	106
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	107
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	0
	.align	1
string.11:
	.byte	99
	.byte	104
	.byte	101
	.byte	99
	.byte	107
	.byte	10
	.byte	0

	.align	4
	.global	main
main:
	save	%sp,-96,%sp
	sethi	%hi(i),%i1
	or	%i1,%lo(i),%i1
	mov	1,%i0
	st	%i0,[%i1]
	sethi	%hi(i),%i0
	or	%i0,%lo(i),%i0
	ld	[%i0],%i0
	add	%i0,1,%i0
	sethi	%hi(j),%i1
	or	%i1,%lo(j),%i1
	st	%i0,[%i1]
	sethi	%hi(j),%i0
	or	%i0,%lo(j),%i0
	ld	[%i0],%i1
	sethi	%hi(i),%i0
	or	%i0,%lo(i),%i0
	ld	[%i0],%i0
	add	%i1,%i0,%i0
	sethi	%hi(k),%i1
	or	%i1,%lo(k),%i1
	st	%i0,[%i1]
	sethi	%hi(k),%i0
	or	%i0,%lo(k),%i0
	ld	[%i0],%i0
	add	%i0,1,%i0
	sethi	%hi(i),%i1
	or	%i1,%lo(i),%i1
	st	%i0,[%i1]
	sethi	%hi(i),%i0
	or	%i0,%lo(i),%i0
	ld	[%i0],%i1
	sethi	%hi(k),%i0
	or	%i0,%lo(k),%i0
	ld	[%i0],%i0
	add	%i1,%i0,%i0
	sethi	%hi(j),%i1
	or	%i1,%lo(j),%i1
	st	%i0,[%i1]
	sethi	%hi(k),%i0
	or	%i0,%lo(k),%i0
	ld	[%i0],%i1
	sethi	%hi(j),%i0
	or	%i0,%lo(j),%i0
	ld	[%i0],%i0
	add	%i1,%i0,%i0
	sethi	%hi(k),%i1
	or	%i1,%lo(k),%i1
	st	%i0,[%i1]
	sethi	%hi(i),%i0
	or	%i0,%lo(i),%i0
	ld	[%i0],%o1
	sethi	%hi(j),%i0
	or	%i0,%lo(j),%i0
	ld	[%i0],%o2
	sethi	%hi(k),%i0
	or	%i0,%lo(k),%i0
	ld	[%i0],%o3
	sethi	%hi(string.9),%o0
	or	%o0,%lo(string.9),%o0
	call	printf
	nop
.L22:
	sethi	%hi(k),%i0
	or	%i0,%lo(k),%i0
	ld	[%i0],%i0
	cmp	%i0,3
	bge	.L27
	nop
.L23:
	sethi	%hi(string.11),%o0
	or	%o0,%lo(string.11),%o0
	call	printf
	nop
	sethi	%hi(i),%i0
	or	%i0,%lo(i),%i0
	ld	[%i0],%i0
	add	%i0,1,%i0
	sethi	%hi(i),%i1
	or	%i1,%lo(i),%i1
	st	%i0,[%i1]
	sethi	%hi(j),%i0
	or	%i0,%lo(j),%i0
	ld	[%i0],%i0
	sub	%i0,1,%i0
	sethi	%hi(j),%i1
	or	%i1,%lo(j),%i1
	st	%i0,[%i1]
	sethi	%hi(k),%i0
	or	%i0,%lo(k),%i0
	ld	[%i0],%i1
	sethi	%hi(j),%i0
	or	%i0,%lo(j),%i0
	ld	[%i0],%i0
	add	%i1,%i0,%i0
	sethi	%hi(k),%i1
	or	%i1,%lo(k),%i1
	st	%i0,[%i1]
	sethi	%hi(k),%i0
	or	%i0,%lo(k),%i0
	ld	[%i0],%i0
	cmp	%i0,0
	be	.L25
	nop
.L24:
	sethi	%hi(i),%i1
	or	%i1,%lo(i),%i1
	mov	3,%i0
	st	%i0,[%i1]
	sethi	%hi(i),%i0
	or	%i0,%lo(i),%i0
	ld	[%i0],%i1
	sethi	%hi(j),%i0
	or	%i0,%lo(j),%i0
	ld	[%i0],%i0
	add	%i1,%i0,%i0
	sethi	%hi(j),%i1
	or	%i1,%lo(j),%i1
	st	%i0,[%i1]
	ba	.L22
	nop
.L25:
	sethi	%hi(k),%i1
	or	%i1,%lo(k),%i1
	mov	1,%i0
	st	%i0,[%i1]
	ba	.L22
	nop
.L27:
	mov	0,%i0
	ret
	restore

	.common	m,4,4
	.common	n,4,4
	.common	i,4,4
	.common	k,4,4
	.common	j,4,4
