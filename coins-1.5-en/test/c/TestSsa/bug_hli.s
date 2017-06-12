 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-0.8.1"
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
	je	.L15
.L6:
	cmpl	$13,%ebx
	jne	.L15
.L7:
	movl	4(%esi),%eax
	leal	-1(%eax),%eax
	movl	%eax,4(%esi)
	movl	4(%esi),%eax
	cmpl	$0,%eax
	jge	.L9
.L8:
	pushl	%esi
	call	___srget
	leal	4(%esp),%esp
	jmp	.L10
.L9:
	movl	(%esi),%eax
	movzbl	(%eax),%eax
	movl	(%esi),%ecx
	leal	1(%ecx),%ecx
	movl	%ecx,(%esi)
.L10:
	cmpl	$10,%eax
	jne	.L12
.L11:
	movl	%eax,%ebx
	jmp	.L15
.L12:
	pushl	%esi
	pushl	%eax
	call	_ungetc
	leal	8(%esp),%esp
.L15:
	movl	%ebx,%eax
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.16:
	.byte	120
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	32
	.byte	121
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.18:
	.byte	120
	.byte	32
	.byte	47
	.byte	32
	.byte	121
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	$1,%ebx
	movl	$0,%ecx
	movl	$0,%esi
.L21:
	cmpl	$10,%esi
	jge	.L27
.L22:
	cmpl	%esi,%ecx
	jg	.L26
.L23:
	cmpl	$0,%ecx
	jne	.L26
.L24:
	pushl	%ecx
	pushl	%ebx
	pushl	$_string.16
	call	_printf
	leal	12(%esp),%esp
	jmp	.L28
.L26:
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	leal	1(%esi),%esi
	jmp	.L21
.L27:
	pushl	%eax
	pushl	$_string.18
	call	_printf
	leal	8(%esp),%esp
.L28:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_bug_hli
_bug_hli:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%ebx
	movl	12(%ebp),%ecx
	movl	$0,%esi
.L32:
	cmpl	$10,%esi
	jge	.L38
.L33:
	cmpl	%esi,%ecx
	jg	.L37
.L34:
	cmpl	$0,%ecx
	jne	.L37
.L35:
	pushl	%ecx
	pushl	%ebx
	pushl	$_string.16
	call	_printf
	leal	12(%esp),%esp
	jmp	.L39
.L37:
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	leal	1(%esi),%esi
	jmp	.L32
.L38:
	pushl	%eax
	pushl	$_string.18
	call	_printf
	leal	8(%esp),%esp
.L39:
	popl	%esi
	popl	%ebx
	leave
	ret

