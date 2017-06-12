 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
	.byte	72
	.byte	101
	.byte	108
	.byte	108
	.byte	111
	.byte	32
	.byte	87
	.byte	111
	.byte	114
	.byte	108
	.byte	100
	.byte	46
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$_string.6,%esi
	pushl	%esi
	call	_strlen
	leal	4(%esp),%esp
	leal	1(%eax),%eax
	pushl	%eax
	call	_malloc
	leal	4(%esp),%esp
	movl	%eax,%ebx
	pushl	%esi
	pushl	%ebx
	call	_strcpy
	leal	8(%esp),%esp
	pushl	%ebx
	call	_puts
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%esi
	popl	%ebx
	leave
	ret

