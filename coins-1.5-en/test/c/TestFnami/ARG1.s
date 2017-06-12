 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
	.byte	97
	.byte	114
	.byte	103
	.byte	58
	.byte	32
	.byte	37
	.byte	115
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	leal	-1(%ecx),%ebx
	leal	4(%eax),%esi
.L4:
	movl	%ebx,%eax
	leal	-1(%ebx),%ebx
	cmpl	$0,%eax
	je	.L7
.L6:
	movl	(%esi),%eax
	leal	4(%esi),%esi
	pushl	%eax
	pushl	$_string.7
	call	_printf
	leal	8(%esp),%esp
	jmp	.L4
.L7:
	movl	$0,%eax
	popl	%esi
	popl	%ebx
	leave
	ret

