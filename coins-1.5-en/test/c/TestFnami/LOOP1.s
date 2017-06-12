 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	37
	.byte	120
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$0,%ebx
.L3:
	cmpl	$32,%ebx
	jge	.L5
.L4:
	movl	$-1,%eax
	movl	%ebx,%ecx
	shrl	%cl,%eax
	pushl	%eax
	pushl	$_string.4
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L3
.L5:
	movl	$0,%eax
	popl	%ebx
	leave
	ret

