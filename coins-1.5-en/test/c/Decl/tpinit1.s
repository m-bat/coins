 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.5:
	.byte	97
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	98
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	99
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	100
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	101
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	105
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	120
	.byte	61
	.byte	37
	.byte	102
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	movl	$0,%eax
	fld1
	fstps	-4(%ebp)
	movl	_e,%eax
	movl	%eax,_a
	movl	$1,_b
	fld1
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$0
	pushl	_e
	pushl	_d
	pushl	_c
	pushl	_b
	pushl	_a
	pushl	$_string.5
	call	_printf
	leal	36(%esp),%esp
	movl	_a,%eax
	leave
	ret

	.data
	.align	4
	.global	_b
_b:
	.long	1
	.align	4
	.global	_e
_e:
	.long	2
	.comm	_d,4
	.comm	_a,4
	.comm	_c,4
