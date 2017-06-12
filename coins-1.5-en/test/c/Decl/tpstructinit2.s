 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
	.byte	78
	.byte	111
	.byte	100
	.byte	97
	.byte	0
	.align	1
_string.8:
	.byte	112
	.byte	52
	.byte	61
	.byte	40
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	41
	.byte	32
	.byte	103
	.byte	51
	.byte	91
	.byte	49
	.byte	93
	.byte	61
	.byte	123
	.byte	37
	.byte	115
	.byte	44
	.byte	37
	.byte	100
	.byte	125
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$32,%esp
	movl	$11,-8(%ebp)
	movl	$12,-4(%ebp)
	movl	$_string.6,-24(%ebp)
	movl	$105,-20(%ebp)
	movl	$_p1,%edx
	leal	-16(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	leal	-24(%ebp),%edx
	movl	$_g3+8,%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	pushl	_g3+12
	pushl	_g3+8
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	$_string.8
	call	_printf
	leal	20(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_p1
_p1:
	.long	1
	.long	2
	.align	4
	.global	_p2
_p2:
	.long	2
	.long	3
	.long	4
	.long	5
	.long	6
	.long	7
	.text
	.align	1
_string.9:
	.byte	84
	.byte	97
	.byte	110
	.byte	97
	.byte	107
	.byte	97
	.byte	0
	.data
	.align	4
	.global	_s1
_s1:
	.long	_string.9
	.long	100
	.text
	.align	1
_string.10:
	.byte	83
	.byte	117
	.byte	122
	.byte	117
	.byte	107
	.byte	105
	.byte	0
	.data
	.align	4
	.global	_s2
_s2:
	.long	_string.10
	.long	101
	.text
	.align	1
_string.11:
	.byte	75
	.byte	105
	.byte	109
	.byte	117
	.byte	114
	.byte	97
	.byte	0
	.align	1
_string.12:
	.byte	83
	.byte	101
	.byte	107
	.byte	105
	.byte	0
	.data
	.align	4
	.global	_g1
_g1:
	.long	_string.11
	.long	102
	.long	_string.12
	.long	204
	.comm	_pa,80
	.comm	_g3,80
