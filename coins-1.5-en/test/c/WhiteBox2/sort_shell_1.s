 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
___sgetc:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	movl	4(%esi),%eax
	leal	-1(%eax),%eax
	movl	%eax,4(%esi)
	movl	4(%esi),%eax
	cmpl	$0,%eax
	jge	.L4
.L3:
	pushl	%esi
	call	___srget
	movl	%eax,%ebx
	leal	4(%esp),%esp
	jmp	.L5
.L4:
	movl	(%esi),%eax
	movzbl	(%eax),%ebx
	movl	(%esi),%eax
	leal	1(%eax),%eax
	movl	%eax,(%esi)
.L5:
	movswl	12(%esi),%eax
	andl	$16384,%eax
	cmpl	$0,%eax
	je	.L18
.L6:
	cmpl	$13,%ebx
	jne	.L8
.L7:
	movl	$1,%eax
	jmp	.L9
.L8:
	movl	$0,%eax
.L9:
	movw	%ax,%ax
	movswl	%ax,%eax
	cmpl	$0,%eax
	je	.L18
.L10:
	movl	4(%esi),%eax
	leal	-1(%eax),%eax
	movl	%eax,4(%esi)
	movl	4(%esi),%eax
	cmpl	$0,%eax
	jge	.L12
.L11:
	pushl	%esi
	call	___srget
	leal	4(%esp),%esp
	jmp	.L13
.L12:
	movl	(%esi),%eax
	movzbl	(%eax),%eax
	movl	(%esi),%ecx
	leal	1(%ecx),%ecx
	movl	%ecx,(%esi)
.L13:
	cmpl	$10,%eax
	jne	.L15
.L14:
	movl	%eax,%ebx
	jmp	.L18
.L15:
	pushl	%esi
	pushl	%eax
	call	_ungetc
	leal	8(%esp),%esp
.L18:
	movl	%ebx,%eax
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.13:
	.byte	37
	.byte	56
	.byte	100
	.byte	32
	.byte	0
	.align	1
_string.15:
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$400,%esp
	pushl	%ebx
	movl	$0,%eax
	movl	$0,%eax
	movl	$0,%eax
	movl	$0,%ebx
.L22:
	cmpl	$100,%ebx
	jge	.L24
.L23:
	call	_rand
	movl	%eax,-400(%ebp,%ebx,4)
	leal	1(%ebx),%ebx
	jmp	.L22
.L24:
	movl	$1,%edx
.L25:
	cmpl	$100,%edx
	jge	.L33
.L26:
	leal	-1(%edx),%ecx
.L27:
	cmpl	$0,%ecx
	jl	.L32
.L28:
	leal	1(%ecx),%eax
	movl	-400(%ebp,%ecx,4),%ebx
	cmpl	-400(%ebp,%eax,4),%ebx
	jle	.L32
.L29:
	movl	-400(%ebp,%ecx,4),%ebx
	leal	1(%ecx),%eax
	movl	-400(%ebp,%eax,4),%eax
	movl	%eax,-400(%ebp,%ecx,4)
	leal	1(%ecx),%eax
	movl	%ebx,-400(%ebp,%eax,4)
	leal	-1(%ecx),%ecx
	jmp	.L27
.L32:
	leal	1(%edx),%edx
	jmp	.L25
.L33:
	movl	$0,%ebx
.L34:
	cmpl	$100,%ebx
	jge	.L36
.L35:
	pushl	-400(%ebp,%ebx,4)
	pushl	$_string.13
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L34
.L36:
	pushl	$_string.15
	call	_printf
	leal	4(%esp),%esp
.L37:
	popl	%ebx
	leave
	ret

