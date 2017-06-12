 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_func
_func:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%edx
	movl	12(%ebp),%ecx
	movl	$0,(%edx)
	movl	$1,4(%edx)
	movl	$1,%eax
	movl	(%edx,%ecx,4),%eax
	leal	1(%eax),%eax
	leave
	ret

	.align	1
_string.13:
	.byte	103
	.byte	49
	.byte	32
	.byte	37
	.byte	100
	.byte	51
	.byte	32
	.byte	103
	.byte	50
	.byte	32
	.byte	37
	.byte	100
	.byte	51
	.byte	32
	.byte	10
	.byte	0
	.align	1
_string.14:
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	0
	.align	1
_string.16:
	.byte	40
	.byte	40
	.byte	99
	.byte	104
	.byte	97
	.byte	114
	.byte	42
	.byte	41
	.byte	49
	.byte	48
	.byte	41
	.byte	91
	.byte	106
	.byte	49
	.byte	93
	.byte	32
	.byte	37
	.byte	99
	.byte	10
	.byte	0
	.align	1
_string.18:
	.byte	49
	.byte	48
	.byte	91
	.byte	106
	.byte	50
	.byte	93
	.byte	32
	.byte	37
	.byte	99
	.byte	10
	.byte	0
	.align	1
_string.20:
	.byte	97
	.byte	98
	.byte	99
	.byte	100
	.byte	101
	.byte	102
	.byte	91
	.byte	51
	.byte	93
	.byte	32
	.byte	37
	.byte	99
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	movl	$3,-4(%ebp)
	pushl	$2
	pushl	$_a1
	call	_func
	leal	8(%esp),%esp
	pushl	$1
	pushl	$_a1
	call	_func
	movl	%eax,_g1
	leal	8(%esp),%esp
	movl	$_g1,_pg1
	leal	-4(%ebp),%eax
	movl	%eax,_pg2
	movl	_pg1,%eax
	movl	(%eax),%ecx
	movl	_pg2,%eax
	addl	(%eax),%ecx
	movl	%ecx,_g2
	pushl	_g2
	pushl	_g1
	pushl	$_string.13
	call	_printf
	leal	12(%esp),%esp
	movl	$_c1,%ecx
	movb	_string.14+2,%al
	movb	%al,10(%ecx)
	movsbl	10(%ecx),%eax
	pushl	%eax
	pushl	$_string.16
	call	_printf
	leal	8(%esp),%esp
	movl	_g1,%eax
	cmpl	$0,%eax
	jne	.L7
.L6:
	movl	$_a1,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movsbl	_string.14+3,%eax
	movl	%eax,10(,%edx,4)
	movl	100,%eax
	pushl	10(,%edx,4)
	pushl	$_string.18
	call	_printf
	leal	8(%esp),%esp
.L7:
	movsbl	_string.14+3,%eax
	pushl	%eax
	pushl	$_string.20
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_x,8
	.comm	_a2,800
	.comm	_a1,80
	.comm	_pg2,4
	.comm	_c1,100
	.comm	_g3,4
	.comm	_pg1,4
	.comm	_g1,4
	.comm	_g4,4
	.comm	_g2,4
