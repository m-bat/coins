 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movl	_count.1,%eax
	leal	1(%eax),%eax
	movl	%eax,_count.1
	movl	_count.1,%eax
	leave
	ret

	.data
	.align	4
_count.1:
	.long	0
	.text
	.align	1
_string.6:
	.byte	37
	.byte	100
	.byte	32
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
	call	_f
	movl	%eax,_a
	call	_f
	movl	%eax,_b
	call	_f
	movl	%eax,_d.2
	pushl	_d.2
	pushl	_b
	pushl	_a
	pushl	$_string.6
	call	_printf
	leal	16(%esp),%esp
.L6:
	leave
	ret

	.data
	.align	4
_d.2:
	.long	0
	.lcomm	_a,4
	.lcomm	_b,4
	.lcomm	_c,40
