 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.16:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movb	8(%ebp),%ch
	movb	12(%ebp),%cl
	movb	16(%ebp),%al
	movzbl	%al,%eax
	pushl	%eax
	movsbl	%cl,%eax
	pushl	%eax
	movsbl	%ch,%eax
	pushl	%eax
	pushl	$_string.16
	call	_printf
	leal	16(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.18:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_g
_g:
	pushl	%ebp
	movl	%esp,%ebp
	movw	8(%ebp),%cx
	movw	12(%ebp),%ax
	movzwl	%ax,%eax
	pushl	%eax
	movswl	%cx,%eax
	pushl	%eax
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
.L6:
	leave
	ret


	.align	4
	.global	_h
_h:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	pushl	%eax
	pushl	%ecx
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
.L9:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_i2
	pushl	_i1
	pushl	_i0
	pushl	$_string.16
	call	_printf
	leal	16(%esp),%esp
	movb	$-16,%al
	movb	$-16,%al
	movb	$-16,%al
	pushl	$240
	pushl	$-16
	pushl	$-16
	pushl	$_string.16
	call	_printf
	leal	16(%esp),%esp
	pushl	$240
	pushl	$-16
	pushl	$-16
	pushl	$_string.16
	call	_printf
	leal	16(%esp),%esp
	pushl	$160
	pushl	$160
	pushl	$160
	call	_f
	leal	12(%esp),%esp
	pushl	_i4
	pushl	_i3
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
	movw	$-3805,%ax
	movw	$-3805,%ax
	pushl	$61731
	pushl	$-3805
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
	pushl	$61731
	pushl	$-3805
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
	pushl	$62550
	pushl	$62550
	call	_g
	leal	8(%esp),%esp
	pushl	_i6
	pushl	_i5
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
	movl	$128,%eax
	movl	$32767,%eax
	pushl	$32767
	pushl	$128
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
	pushl	$32767
	pushl	$128
	pushl	$_string.18
	call	_printf
	leal	12(%esp),%esp
	pushl	$32767
	pushl	$128
	call	_h
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_i0
_i0:
	.long	-1
	.align	4
	.global	_i1
_i1:
	.long	-1
	.align	4
	.global	_i2
_i2:
	.long	255
	.align	4
	.global	_i3
_i3:
	.long	-1
	.align	4
	.global	_i4
_i4:
	.long	65535
	.align	4
	.global	_i5
_i5:
	.long	128
	.align	4
	.global	_i6
_i6:
	.long	32767
