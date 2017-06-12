 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.5:
	.byte	97
	.byte	91
	.byte	37
	.byte	100
	.byte	93
	.byte	91
	.byte	37
	.byte	100
	.byte	93
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	98
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0
	.align	1
_string.7:
	.byte	114
	.byte	101
	.byte	116
	.byte	117
	.byte	114
	.byte	110
	.byte	32
	.byte	33
	.byte	61
	.byte	32
	.byte	48
	.byte	32
	.byte	45
	.byte	62
	.byte	32
	.byte	39
	.byte	112
	.byte	111
	.byte	105
	.byte	110
	.byte	116
	.byte	101
	.byte	114
	.byte	32
	.byte	116
	.byte	111
	.byte	32
	.byte	105
	.byte	110
	.byte	116
	.byte	101
	.byte	103
	.byte	101
	.byte	114
	.byte	32
	.byte	119
	.byte	47
	.byte	111
	.byte	32
	.byte	99
	.byte	97
	.byte	115
	.byte	116
	.byte	39
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$20,%esp
	movl	$7,-20(%ebp)
	movl	$8,-16(%ebp)
	movl	$9,-12(%ebp)
	movl	$0,-8(%ebp)
	movl	$10,-4(%ebp)
	movl	$0,%eax
	movl	_d,%edx
	imull	$12,%edx
	movl	_e,%ecx
	movl	_e,%eax
	movl	%eax,_a(%edx,%ecx,4)
	movl	_stx1+4,%eax
	movl	%eax,_b
	pushl	_stx1+4
	movl	_d,%ecx
	imull	$12,%ecx
	movl	_e,%eax
	pushl	_a(%ecx,%eax,4)
	pushl	_e
	pushl	_d
	pushl	$_string.5
	call	_printf
	leal	20(%esp),%esp
	pushl	$_string.7
	call	_printf
	leal	4(%esp),%esp
	movl	_a,%eax
	leave
	ret

	.data
	.align	4
	.global	_a
_a:
	.long	0
	.long	1
	.long	2
	.long	3
	.long	4
	.long	5
	.align	4
	.global	_stx1
_stx1:
	.long	1
	.long	2
	.long	3
	.long	4
	.long	5
	.align	4
	.global	_e
_e:
	.long	2
	.comm	_c,4
	.comm	_b,4
	.comm	_d,4
