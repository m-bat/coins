 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86_64 convention:mac */

	.section .text
	.align	4
__sgetc:

	pushq	%rbx
	pushq	%r12
	subq	$8,%rsp
	movq	%rdi,%r12
	movl	8(%r12),%eax
	leal	-1(%rax),%eax
	movl	%eax,8(%r12)
	movslq	8(%r12),%rax
	cmpq	$0,%rax
	jge	.L4
.L3:
	movq	%r12,%rdi
	call	__srget
	movl	%eax,%ebx
	jmp	.L5
.L4:
	movq	(%r12),%rax
	movzbl	(%rax),%ebx
	movq	(%r12),%rax
	leaq	1(%rax),%rax
	movq	%rax,(%r12)
.L5:
	movswl	16(%r12),%eax
	andl	$16384,%eax
	cmpl	$0,%eax
	je	.L18
.L6:
	cmpl	$13,%ebx
	jne	.L8
.L7:
	leal	1,%eax
	jmp	.L9
.L8:
	leal	0,%eax
.L9:
	movswl	%ax,%eax
	cmpl	$0,%eax
	je	.L18
.L10:
	movl	8(%r12),%eax
	leal	-1(%rax),%eax
	movl	%eax,8(%r12)
	movslq	8(%r12),%rax
	cmpq	$0,%rax
	jge	.L12
.L11:
	movq	%r12,%rdi
	call	__srget
	jmp	.L13
.L12:
	movq	(%r12),%rax
	movzbl	(%rax),%eax
	movq	(%r12),%rcx
	leaq	1(%rcx),%rcx
	movq	%rcx,(%r12)
.L13:
	cmpl	$10,%eax
	jne	.L15
.L14:
	movl	%eax,%ebx
	jmp	.L18
.L15:
	movl	%eax,%edi
	movq	%r12,%rsi
	call	ungetc
.L18:
	movl	%ebx,%eax
	addq	$8,%rsp
	popq	%r12
	popq	%rbx
	ret


	.align	4
	.global	rotate1
rotate1:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$40,%rsp
	pushq	%rbx
	pushq	%r12
	pushq	%r13
	pushq	%r14
	pushq	%r15
	movq	%rdi,%r14
	movq	%rsi,%r13
	movq	%rdx,%r12
	movq	%rcx,%rbx
	movl	%r8d,-28(%rbp)
	movsd	%xmm0,-24(%rbp)
	leal	0,%r15d
.L22:
	cmpl	-28(%rbp),%r15d
	jge	.L24
.L23:
	movsd	-24(%rbp),%xmm0
	call	cos
	movsd	%xmm0,-8(%rbp)
	movsd	-24(%rbp),%xmm0
	call	sin
	movslq	%r15d,%rax
	movsd	(%r14,%rax,8),%xmm2
	mulsd	-8(%rbp),%xmm2
	movslq	%r15d,%rax
	movsd	(%r13,%rax,8),%xmm1
	mulsd	%xmm0,%xmm1
	addsd	%xmm1,%xmm2
	movslq	%r15d,%rax
	movsd	%xmm2,(%r12,%rax,8)
	movsd	-24(%rbp),%xmm0
	call	sin
	movsd	%xmm0,-16(%rbp)
	movsd	-24(%rbp),%xmm0
	call	cos
	movslq	%r15d,%rax
	movsd	(%r14,%rax,8),%xmm2
	movl	$128,%edx
	movd	%edx,%xmm7
	pslldq	$7,%xmm7
	xorpd	%xmm7,%xmm2
	mulsd	-16(%rbp),%xmm2
	movslq	%r15d,%rax
	movsd	(%r13,%rax,8),%xmm1
	mulsd	%xmm0,%xmm1
	addsd	%xmm1,%xmm2
	movslq	%r15d,%rax
	movsd	%xmm2,(%rbx,%rax,8)
	leal	1(%r15),%r15d
	jmp	.L22
.L24:
	leaq	-80(%rbp),%rsp
	popq	%r15
	popq	%r14
	popq	%r13
	popq	%r12
	popq	%rbx
	leave
	ret


	.align	4
	.global	rotate2
rotate2:

	movq	%rdx,%rax
	leal	0,%r9d
.L27:
	cmpl	%r8d,%r9d
	jge	.L29
.L28:
	movslq	%r9d,%rdx
	movsd	(%rdi,%rdx,8),%xmm3
	mulsd	%xmm0,%xmm3
	movslq	%r9d,%rdx
	movsd	(%rsi,%rdx,8),%xmm2
	mulsd	%xmm1,%xmm2
	addsd	%xmm2,%xmm3
	movslq	%r9d,%rdx
	movsd	%xmm3,(%rax,%rdx,8)
	movslq	%r9d,%rdx
	movsd	(%rdi,%rdx,8),%xmm3
	movl	$128,%edx
	movd	%edx,%xmm7
	pslldq	$7,%xmm7
	xorpd	%xmm7,%xmm3
	mulsd	%xmm1,%xmm3
	movslq	%r9d,%rdx
	movsd	(%rsi,%rdx,8),%xmm2
	mulsd	%xmm0,%xmm2
	addsd	%xmm2,%xmm3
	movslq	%r9d,%rdx
	movsd	%xmm3,(%rcx,%rdx,8)
	leal	1(%r9),%r9d
	jmp	.L27
.L29:
	ret


	.align	4
	.global	rotate3
rotate3:

	movq	%rdx,%rax
	leal	0,%r8d
.L32:
	cmpl	$1000,%r8d
	jge	.L34
.L33:
	movslq	%r8d,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	mulsd	.LC1,%xmm1
	movslq	%r8d,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC2,%xmm0
	addsd	%xmm0,%xmm1
	movslq	%r8d,%rdx
	movsd	%xmm1,(%rax,%rdx,8)
	movslq	%r8d,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	movl	$128,%edx
	movd	%edx,%xmm7
	pslldq	$7,%xmm7
	xorpd	%xmm7,%xmm1
	mulsd	.LC2,%xmm1
	movslq	%r8d,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC1,%xmm0
	addsd	%xmm0,%xmm1
	movslq	%r8d,%rdx
	movsd	%xmm1,(%rcx,%rdx,8)
	leal	1(%r8),%r8d
	jmp	.L32
.L34:
	ret


	.align	4
	.global	rotate4
rotate4:

	movq	%rdx,%rax
	leal	0,%r8d
.L37:
	cmpl	$1000,%r8d
	jge	.L39
.L38:
	movslq	%r8d,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	mulsd	.LC1,%xmm1
	movslq	%r8d,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC2,%xmm0
	addsd	%xmm0,%xmm1
	movslq	%r8d,%rdx
	movsd	%xmm1,(%rax,%rdx,8)
	movslq	%r8d,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	movl	$128,%edx
	movd	%edx,%xmm7
	pslldq	$7,%xmm7
	xorpd	%xmm7,%xmm1
	mulsd	.LC2,%xmm1
	movslq	%r8d,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC1,%xmm0
	addsd	%xmm0,%xmm1
	movslq	%r8d,%rdx
	movsd	%xmm1,(%rcx,%rdx,8)
	leal	1(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	mulsd	.LC1,%xmm1
	leal	1(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC2,%xmm0
	addsd	%xmm0,%xmm1
	leal	1(%r8),%edx
	movslq	%edx,%rdx
	movsd	%xmm1,(%rax,%rdx,8)
	leal	1(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	movl	$128,%edx
	movd	%edx,%xmm7
	pslldq	$7,%xmm7
	xorpd	%xmm7,%xmm1
	mulsd	.LC2,%xmm1
	leal	1(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC1,%xmm0
	addsd	%xmm0,%xmm1
	leal	1(%r8),%edx
	movslq	%edx,%rdx
	movsd	%xmm1,(%rcx,%rdx,8)
	leal	2(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	mulsd	.LC1,%xmm1
	leal	2(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC2,%xmm0
	addsd	%xmm0,%xmm1
	leal	2(%r8),%edx
	movslq	%edx,%rdx
	movsd	%xmm1,(%rax,%rdx,8)
	leal	2(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	movl	$128,%edx
	movd	%edx,%xmm7
	pslldq	$7,%xmm7
	xorpd	%xmm7,%xmm1
	mulsd	.LC2,%xmm1
	leal	2(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC1,%xmm0
	addsd	%xmm0,%xmm1
	leal	2(%r8),%edx
	movslq	%edx,%rdx
	movsd	%xmm1,(%rcx,%rdx,8)
	leal	3(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	mulsd	.LC1,%xmm1
	leal	3(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC2,%xmm0
	addsd	%xmm0,%xmm1
	leal	3(%r8),%edx
	movslq	%edx,%rdx
	movsd	%xmm1,(%rax,%rdx,8)
	leal	3(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rdi,%rdx,8),%xmm1
	movl	$128,%edx
	movd	%edx,%xmm7
	pslldq	$7,%xmm7
	xorpd	%xmm7,%xmm1
	mulsd	.LC2,%xmm1
	leal	3(%r8),%edx
	movslq	%edx,%rdx
	movsd	(%rsi,%rdx,8),%xmm0
	mulsd	.LC1,%xmm0
	addsd	%xmm0,%xmm1
	leal	3(%r8),%edx
	movslq	%edx,%rdx
	movsd	%xmm1,(%rcx,%rdx,8)
	leal	4(%r8),%r8d
	jmp	.L37
.L39:
	ret

	.align	1
string.41:
	.byte	114
	.byte	111
	.byte	116
	.byte	97
	.byte	116
	.byte	101
	.byte	49
	.byte	58
	.byte	32
	.byte	78
	.byte	111
	.byte	32
	.byte	111
	.byte	112
	.byte	116
	.byte	105
	.byte	109
	.byte	105
	.byte	122
	.byte	97
	.byte	116
	.byte	105
	.byte	111
	.byte	110
	.byte	32
	.byte	97
	.byte	110
	.byte	103
	.byte	108
	.byte	101
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	99
	.byte	111
	.byte	115
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	115
	.byte	105
	.byte	110
	.byte	32
	.byte	37
	.byte	101
	.byte	10
	.byte	0
	.align	1
string.43:
	.byte	32
	.byte	32
	.byte	32
	.byte	32
	.byte	113
	.byte	120
	.byte	91
	.byte	48
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	32
	.byte	113
	.byte	121
	.byte	91
	.byte	48
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	10
	.byte	0
	.align	1
string.45:
	.byte	114
	.byte	111
	.byte	116
	.byte	97
	.byte	116
	.byte	101
	.byte	50
	.byte	58
	.byte	32
	.byte	67
	.byte	104
	.byte	97
	.byte	110
	.byte	103
	.byte	101
	.byte	32
	.byte	99
	.byte	111
	.byte	115
	.byte	47
	.byte	115
	.byte	105
	.byte	110
	.byte	32
	.byte	116
	.byte	111
	.byte	32
	.byte	99
	.byte	111
	.byte	110
	.byte	115
	.byte	116
	.byte	97
	.byte	110
	.byte	116
	.byte	32
	.byte	10
	.byte	0
	.align	1
string.47:
	.byte	32
	.byte	32
	.byte	32
	.byte	32
	.byte	113
	.byte	120
	.byte	91
	.byte	49
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	32
	.byte	113
	.byte	121
	.byte	91
	.byte	49
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	10
	.byte	0
	.align	1
string.49:
	.byte	114
	.byte	111
	.byte	116
	.byte	97
	.byte	116
	.byte	101
	.byte	51
	.byte	58
	.byte	32
	.byte	69
	.byte	109
	.byte	98
	.byte	101
	.byte	100
	.byte	32
	.byte	99
	.byte	111
	.byte	115
	.byte	47
	.byte	115
	.byte	105
	.byte	110
	.byte	32
	.byte	97
	.byte	110
	.byte	100
	.byte	32
	.byte	99
	.byte	111
	.byte	117
	.byte	110
	.byte	116
	.byte	32
	.byte	97
	.byte	115
	.byte	32
	.byte	99
	.byte	111
	.byte	110
	.byte	115
	.byte	116
	.byte	97
	.byte	110
	.byte	116
	.byte	115
	.byte	32
	.byte	10
	.byte	0
	.align	1
string.51:
	.byte	32
	.byte	32
	.byte	32
	.byte	32
	.byte	113
	.byte	120
	.byte	91
	.byte	50
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	32
	.byte	113
	.byte	121
	.byte	91
	.byte	50
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	10
	.byte	0
	.align	1
string.53:
	.byte	114
	.byte	111
	.byte	116
	.byte	97
	.byte	116
	.byte	101
	.byte	52
	.byte	58
	.byte	32
	.byte	69
	.byte	120
	.byte	112
	.byte	97
	.byte	110
	.byte	100
	.byte	32
	.byte	108
	.byte	111
	.byte	111
	.byte	112
	.byte	32
	.byte	52
	.byte	32
	.byte	116
	.byte	105
	.byte	109
	.byte	101
	.byte	115
	.byte	32
	.byte	97
	.byte	110
	.byte	100
	.byte	32
	.byte	101
	.byte	109
	.byte	98
	.byte	101
	.byte	100
	.byte	32
	.byte	99
	.byte	111
	.byte	110
	.byte	115
	.byte	116
	.byte	97
	.byte	110
	.byte	116
	.byte	115
	.byte	32
	.byte	10
	.byte	0
	.align	1
string.55:
	.byte	32
	.byte	32
	.byte	32
	.byte	32
	.byte	113
	.byte	120
	.byte	91
	.byte	51
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	32
	.byte	113
	.byte	121
	.byte	91
	.byte	51
	.byte	93
	.byte	32
	.byte	37
	.byte	101
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	main
main:

	pushq	%rbx
	leal	0,%ecx
.L42:
	cmpl	$1000,%ecx
	jge	.L44
.L43:
	leaq	1(%rcx),%rax
	cvtsi2sdq	%rax,%xmm0
	movslq	%ecx,%rax
	movsd	%xmm0,px(,%rax,8)
	leaq	1(,%rcx,2),%rax
	cvtsi2sdq	%rax,%xmm0
	movslq	%ecx,%rax
	movsd	%xmm0,py(,%rax,8)
	leal	1(%rcx),%ecx
	jmp	.L42
.L44:
	movsd	.LC3,%xmm0
	movsd	%xmm0,angle(%rip)
	movsd	angle(%rip),%xmm0
	call	cos
	movsd	%xmm0,cos_a(%rip)
	movsd	angle(%rip),%xmm0
	call	sin
	movsd	%xmm0,sin_a(%rip)
	movsd	angle(%rip),%xmm0
	movsd	cos_a(%rip),%xmm1
	movsd	sin_a(%rip),%xmm2
	leaq	string.41(%rip),%rdi
	leal	3,%eax
	call	printf
	leal	0,%ebx
.L45:
	cmpl	$1000,%ebx
	jge	.L47
.L46:
	movsd	angle(%rip),%xmm0
	leaq	px(%rip),%rdi
	leaq	py(%rip),%rsi
	leaq	qx(%rip),%rdx
	leaq	qy(%rip),%rcx
	leal	1000,%r8d
	call	rotate1
	leal	1(%rbx),%ebx
	jmp	.L45
.L47:
	movsd	qx(%rip),%xmm0
	movsd	qy(%rip),%xmm1
	leaq	string.43(%rip),%rdi
	leal	2,%eax
	call	printf
	leaq	string.45(%rip),%rdi
	leal	0,%eax
	call	printf
	leal	0,%ebx
.L48:
	cmpl	$1000,%ebx
	jge	.L50
.L49:
	movsd	cos_a(%rip),%xmm0
	movsd	sin_a(%rip),%xmm1
	leaq	px(%rip),%rdi
	leaq	py(%rip),%rsi
	leaq	qx(%rip),%rdx
	leaq	qy(%rip),%rcx
	leal	1000,%r8d
	call	rotate2
	leal	1(%rbx),%ebx
	jmp	.L48
.L50:
	movsd	qx+8(%rip),%xmm0
	movsd	qy+8(%rip),%xmm1
	leaq	string.47(%rip),%rdi
	leal	2,%eax
	call	printf
	leaq	string.49(%rip),%rdi
	leal	0,%eax
	call	printf
	leal	0,%ebx
.L51:
	cmpl	$1000,%ebx
	jge	.L53
.L52:
	leaq	px(%rip),%rdi
	leaq	py(%rip),%rsi
	leaq	qx(%rip),%rdx
	leaq	qy(%rip),%rcx
	call	rotate3
	leal	1(%rbx),%ebx
	jmp	.L51
.L53:
	movsd	qx+16(%rip),%xmm0
	movsd	qy+16(%rip),%xmm1
	leaq	string.51(%rip),%rdi
	leal	2,%eax
	call	printf
	leaq	string.53(%rip),%rdi
	leal	0,%eax
	call	printf
	leal	0,%ebx
.L54:
	cmpl	$1000,%ebx
	jge	.L56
.L55:
	leaq	px(%rip),%rdi
	leaq	py(%rip),%rsi
	leaq	qx(%rip),%rdx
	leaq	qy(%rip),%rcx
	call	rotate4
	leal	1(%rbx),%ebx
	jmp	.L54
.L56:
	movsd	qx+24(%rip),%xmm0
	movsd	qy+24(%rip),%xmm1
	leaq	string.55(%rip),%rdi
	leal	2,%eax
	call	printf
	leal	0,%eax
	popq	%rbx
	ret

	.comm	px,8000,8
	.comm	py,8000,8
	.comm	cos_a,8,8
	.comm	qy,8000,8
	.comm	qx,8000,8
	.comm	angle,8,8
	.comm	sin_a,8,8
	.align	8
.LC1:
	.long	0x89c86b3e,0x3fef62c0 /* 0.9808047 */
	.align	8
.LC2:
	.long	0x52bab7f7,0x3fc8f585 /* 0.1949927 */
	.align	8
.LC3:
	.long	0x51eb851f,0x3fc91eb8 /* 0.19625 */
