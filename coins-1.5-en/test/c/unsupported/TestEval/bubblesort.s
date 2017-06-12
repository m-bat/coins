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
string.19:
	.byte	37
	.byte	100
	.byte	32
	.byte	32
	.byte	0
	.align	1
string.22:
	.byte	10
	.byte	0

	.align	4
	.global	main
main:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$8024,%rsp
	pushq	%rbx
	call	__getreent
	movq	16(%rax),%rdi
	call	fflush
	leal	0,%ebx
.L22:
	cmpl	$1000,%ebx
	jge	.L24
.L23:
	call	rand
	leal	1000,%ecx
	cltd
	idivl	%ecx
	movslq	%ebx,%rcx
	leal	1(%rdx),%eax
	movl	%eax,-8008(%rbp,%rcx,4)
	leal	1(%rbx),%ebx
	jmp	.L22
.L24:
	leal	0,%edx
.L25:
	cmpl	$10,%edx
	jge	.L43
.L26:
	leal	0,%esi
.L27:
	cmpl	$1000,%esi
	jge	.L29
.L28:
	movslq	%esi,%rax
	movl	-8008(%rbp,%rax,4),%eax
	movslq	%esi,%rcx
	movl	%eax,-4004(%rbp,%rcx,4)
	leal	1(%rsi),%esi
	jmp	.L27
.L29:
	leal	0,%edi
.L30:
	cmpl	$1000,%edi
	jge	.L37
.L31:
	leal	0,%esi
.L32:
	cmpl	$999,%esi
	jge	.L36
.L33:
	movslq	%esi,%rax
	movl	-4004(%rbp,%rax,4),%ecx
	leal	1(%rsi),%eax
	movslq	%eax,%rax
	cmpl	-4004(%rbp,%rax,4),%ecx
	jle	.L35
.L34:
	movslq	%esi,%rax
	movl	-4004(%rbp,%rax,4),%r8d
	leal	1(%rsi),%eax
	movslq	%eax,%rax
	movl	-4004(%rbp,%rax,4),%eax
	movslq	%esi,%rcx
	movl	%eax,-4004(%rbp,%rcx,4)
	leal	1(%rsi),%eax
	movslq	%eax,%rax
	movl	%r8d,-4004(%rbp,%rax,4)
.L35:
	leal	1(%rsi),%esi
	jmp	.L32
.L36:
	leal	1(%rdi),%edi
	jmp	.L30
.L37:
	leal	1(%rdx),%edx
	jmp	.L25
.L43:
	leal	0,%ebx
.L44:
	cmpl	$10,%ebx
	jge	.L46
.L45:
	leaq	string.19(%rip),%rdi
	movslq	%ebx,%rax
	movl	-4004(%rbp,%rax,4),%esi
	leal	0,%eax
	call	printf
	leal	1(%rbx),%ebx
	jmp	.L44
.L46:
	leaq	string.22(%rip),%rdi
	leal	0,%eax
	call	printf
	leal	990,%ebx
.L47:
	cmpl	$1000,%ebx
	jge	.L49
.L48:
	leaq	string.19(%rip),%rdi
	movslq	%ebx,%rax
	movl	-4004(%rbp,%rax,4),%esi
	leal	0,%eax
	call	printf
	leal	1(%rbx),%ebx
	jmp	.L47
.L49:
	leaq	string.22(%rip),%rdi
	leal	0,%eax
	call	printf
	leal	0,%edi
	call	exit
.L50:
	leaq	-8032(%rbp),%rsp
	popq	%rbx
	leave
	ret

