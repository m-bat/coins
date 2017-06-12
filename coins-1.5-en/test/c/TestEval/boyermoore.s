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

	.align	1
string.34:
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	0
	.align	1
string.37:
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	103
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	0
	.align	1
string.39:
	.byte	116
	.byte	101
	.byte	120
	.byte	116
	.byte	91
	.byte	48
	.byte	45
	.byte	53
	.byte	57
	.byte	93
	.byte	61
	.byte	0
	.align	1
string.41:
	.byte	37
	.byte	99
	.byte	0
	.align	1
string.43:
	.byte	10
	.byte	0
	.align	1
string.45:
	.byte	116
	.byte	101
	.byte	120
	.byte	116
	.byte	91
	.byte	37
	.byte	100
	.byte	45
	.byte	37
	.byte	100
	.byte	93
	.byte	61
	.byte	0
	.align	1
string.50:
	.byte	103
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	0
	.align	1
string.53:
	.byte	110
	.byte	111
	.byte	116
	.byte	32
	.byte	102
	.byte	111
	.byte	117
	.byte	110
	.byte	100
	.byte	10
	.byte	0
	.align	1
string.55:
	.byte	102
	.byte	111
	.byte	117
	.byte	110
	.byte	100
	.byte	32
	.byte	105
	.byte	110
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	main
main:

	pushq	%rbx
	pushq	%r12
	subq	$8,%rsp
	leaq	text(%rip),%rdi
	leaq	string.34(%rip),%rsi
	call	strcpy
	leal	30,%ebx
.L22:
	cmpl	$9970,%ebx
	jge	.L24
.L23:
	movslq	%ebx,%rax
	leaq	text(%rax),%rdi
	leaq	string.34(%rip),%rsi
	call	strcat
	leal	30(%rbx),%ebx
	jmp	.L22
.L24:
	movb	$0,text+9968(%rip)
	leaq	text+9968(%rip),%rdi
	leaq	string.37(%rip),%rsi
	call	strcat
	leaq	string.39(%rip),%rdi
	leal	0,%eax
	call	printf
	leal	0,%ebx
.L25:
	cmpl	$60,%ebx
	jge	.L27
.L26:
	leaq	string.41(%rip),%rdi
	movslq	%ebx,%rax
	movsbl	text(%rax),%esi
	leal	0,%eax
	call	printf
	leal	1(%rbx),%ebx
	jmp	.L25
.L27:
	leaq	string.43(%rip),%rdi
	leal	0,%eax
	call	printf
	leaq	string.45(%rip),%rdi
	leal	9968,%esi
	leal	9999,%edx
	leal	0,%eax
	call	printf
	leal	9968,%ebx
.L28:
	cmpl	$10000,%ebx
	jge	.L30
.L29:
	leaq	string.41(%rip),%rdi
	movslq	%ebx,%rax
	movsbl	text(%rax),%eax
	movl	%eax,%esi
	leal	0,%eax
	call	printf
	leal	1(%rbx),%ebx
	jmp	.L28
.L30:
	leaq	string.43(%rip),%rdi
	leal	0,%eax
	call	printf
	leaq	text(%rip),%rdi
	call	strlen
	movl	%eax,n(%rip)
	leaq	pattern(%rip),%rdi
	leaq	string.50(%rip),%rsi
	call	strcpy
	leaq	pattern(%rip),%rdi
	call	strlen
	movl	%eax,m(%rip)
	movl	m(%rip),%edi
	call	InitSkip
	movl	m(%rip),%edi
	call	InitNext
	leaq	0,%r12
.L31:
	cmpq	$100,%r12
	jge	.L33
.L32:
	movl	m(%rip),%edi
	movl	n(%rip),%esi
	call	BmMatch
	movl	%eax,%ebx
	leaq	1(%r12),%r12
	jmp	.L31
.L33:
	cmpl	$0,%ebx
	jge	.L35
.L34:
	leaq	string.53(%rip),%rdi
	leal	0,%eax
	call	printf
	jmp	.L36
.L35:
	leaq	string.55(%rip),%rdi
	movl	%ebx,%esi
	leal	0,%eax
	call	printf
.L36:
	leal	0,%eax
	addq	$8,%rsp
	popq	%r12
	popq	%rbx
	ret


	.align	4
	.global	max
max:

	cmpl	%esi,%edi
	jge	.L41
.L40:
	jmp	.L42
.L41:
	movl	%edi,%esi
.L42:
	movl	%esi,%eax
	ret


	.align	4
	.global	min
min:

	cmpl	%esi,%edi
	jge	.L47
.L46:
	jmp	.L48
.L47:
	movl	%esi,%edi
.L48:
	movl	%edi,%eax
	ret


	.align	4
	.global	InitSkip
InitSkip:

	leal	0,%ecx
.L52:
	cmpl	$256,%ecx
	jge	.L54
.L53:
	movslq	%ecx,%rax
	movl	%edi,skip(,%rax,4)
	leal	1(%rcx),%ecx
	jmp	.L52
.L54:
	leal	0,%edx
.L55:
	leal	-1(%rdi),%eax
	cmpl	%eax,%edx
	jge	.L57
.L56:
	movslq	%edx,%rax
	movsbl	pattern(%rax),%eax
	andl	$255,%eax
	movslq	%eax,%rcx
	leal	-1(%rdi),%eax
	subl	%edx,%eax
	movl	%eax,skip(,%rcx,4)
	leal	1(%rdx),%edx
	jmp	.L55
.L57:
	ret


	.align	4
	.global	InitNext
InitNext:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$216,%rsp
	pushq	%rbx
	pushq	%r12
	pushq	%r13
	movl	%edi,%r12d
	leal	0,%edx
.L60:
	cmpl	%r12d,%edx
	jge	.L62
.L61:
	leal	(,%r12,2),%eax
	leal	-1(%rax),%eax
	subl	%edx,%eax
	movslq	%edx,%rcx
	movl	%eax,next(,%rcx,4)
	leal	1(%rdx),%edx
	jmp	.L60
.L62:
	movl	%r12d,%ebx
	leal	-1(%r12),%r13d
.L63:
	cmpl	$0,%r13d
	jl	.L68
.L64:
	movslq	%r13d,%rax
	movl	%ebx,-200(%rbp,%rax,4)
	movslq	%r13d,%rax
	movb	pattern(%rax),%al
	movslq	%r12d,%rcx
	movb	%al,pattern(%rcx)
.L65:
	movslq	%ebx,%rax
	movb	pattern(%rax),%cl
	movslq	%r13d,%rax
	cmpb	pattern(%rax),%cl
	je	.L67
.L66:
	movslq	%ebx,%rax
	movl	next(,%rax,4),%ecx
	leal	-1(%r12),%eax
	subl	%r13d,%eax
	movl	%ecx,%edi
	movl	%eax,%esi
	call	min
	movslq	%ebx,%rcx
	movl	%eax,next(,%rcx,4)
	movslq	%ebx,%rax
	movl	-200(%rbp,%rax,4),%ebx
	jmp	.L65
.L67:
	leal	-1(%rbx),%ebx
	leal	-1(%r13),%r13d
	jmp	.L63
.L68:
	leal	0,%r13d
.L69:
	cmpl	%r12d,%r13d
	jge	.L73
.L70:
	movslq	%r13d,%rax
	movl	next(,%rax,4),%eax
	leal	(%rbx,%r12),%esi
	subl	%r13d,%esi
	movl	%eax,%edi
	call	min
	movslq	%r13d,%rcx
	movl	%eax,next(,%rcx,4)
	cmpl	%ebx,%r13d
	jl	.L72
.L71:
	movslq	%ebx,%rax
	movl	-200(%rbp,%rax,4),%ebx
.L72:
	leal	1(%r13),%r13d
	jmp	.L69
.L73:
	leaq	-240(%rbp),%rsp
	popq	%r13
	popq	%r12
	popq	%rbx
	leave
	ret


	.align	4
	.global	BmMatch
BmMatch:

	pushq	%rbx
	pushq	%r12
	pushq	%r13
	movl	%edi,%r12d
	movl	%esi,%ebx
	leal	-1(%r12),%r13d
.L77:
	cmpl	%ebx,%r13d
	jge	.L89
.L78:
	leal	-1(%r12),%edx
.L80:
	cmpl	$0,%edx
	jl	.L86
.L81:
	movslq	%r13d,%rax
	movb	text(%rax),%cl
	movslq	%edx,%rax
	cmpb	pattern(%rax),%cl
	jne	.L86
.L85:
	leal	-1(%r13),%r13d
	leal	-1(%rdx),%edx
	jmp	.L80
.L86:
	cmpl	$0,%edx
	jge	.L88
.L87:
	leal	1(%r13),%eax
	jmp	.L90
.L88:
	movslq	%r13d,%rax
	movsbl	text(%rax),%eax
	andl	$255,%eax
	movslq	%eax,%rax
	movl	skip(,%rax,4),%eax
	movslq	%edx,%rcx
	movl	next(,%rcx,4),%esi
	movl	%eax,%edi
	call	max
	leal	(%r13,%rax),%r13d
	jmp	.L77
.L89:
	leal	-1,%eax
.L90:
	popq	%r13
	popq	%r12
	popq	%rbx
	ret

	.comm	skip,1024,4
	.comm	n,4,4
	.comm	m,4,4
	.comm	next,200,4
	.comm	text,10000,1
	.comm	pattern,50,1
