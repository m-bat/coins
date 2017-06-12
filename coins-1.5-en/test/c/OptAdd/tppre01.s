 .ident "Coins Compiler version: coins-1.4.4 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.10:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	_data+4,%edx
	movl	_data+8,%eax
	cmpl	$0,%edx
	je	.L4
.L3:
	leal	(%edx,%eax),%eax
	movl	%eax,%ecx
	jmp	.L5
.L4:
	leal	1(%edx),%ecx
	leal	(%edx,%eax),%eax
.L5:
	pushl	%eax
	pushl	%ecx
	pushl	$_string.10
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_data
_data:
	.long	0
	.long	1
	.long	2
	.long	3
	.long	4
	.long	5
	.long	6
	.long	7
	.long	8
	.long	9
