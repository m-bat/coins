 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.17:
	.byte	102
	.byte	40
	.byte	41
	.byte	10
	.byte	0

	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$_string.17
	call	_printf
	leal	4(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.19:
	.byte	103
	.byte	40
	.byte	41
	.byte	10
	.byte	0

	.align	4
	.global	_g
_g:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$_string.19
	call	_printf
	leal	4(%esp),%esp
.L6:
	leave
	ret

	.align	1
_string.21:
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.26:
	.byte	110
	.byte	117
	.byte	108
	.byte	108
	.byte	10
	.byte	0

	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$16,%esp
	fldl	8(%ebp)
	fstpl	-16(%ebp)
	fldl	-16(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L10
.L9:
	movl	$10,%eax
	jmp	.L11
.L10:
	movl	$20,%eax
.L11:
	pushl	%eax
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-16(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L13
.L12:
	movl	$_s0,%edx
	leal	-8(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	jmp	.L14
.L13:
	movl	$_s1,%edx
	leal	-8(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
.L14:
	pushl	-4(%ebp)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-16(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L16
.L15:
	call	_f
	jmp	.L17
.L16:
	call	_g
.L17:
	fldl	-16(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L19
.L18:
	movl	$_s0,%eax
	jmp	.L20
.L19:
	movl	$_s1,%eax
.L20:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-16(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L22
.L21:
	movl	$_s0,%eax
	jmp	.L23
.L22:
	movl	$0,%eax
.L23:
	cmpl	$0,%eax
	je	.L25
.L24:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	jmp	.L26
.L25:
	pushl	$_string.26
	call	_printf
	leal	4(%esp),%esp
.L26:
	fldl	-16(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L28
.L27:
	movl	$_s0,%eax
	jmp	.L29
.L28:
	movl	$_s1,%eax
.L29:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
.L30:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	$10
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	pushl	_s0+4
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	call	_f
	pushl	_s0+4
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	movl	$_s0,%eax
	cmpl	$0,%eax
	je	.L34
.L33:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	jmp	.L35
.L34:
	pushl	$_string.26
	call	_printf
	leal	4(%esp),%esp
.L35:
	movl	$_s0,%eax
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	pushl	$20
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	pushl	_s1+4
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	call	_g
	pushl	_s1+4
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	pushl	$_string.26
	call	_printf
	leal	4(%esp),%esp
	movl	$_s1,%eax
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fld1
	fstpl	-24(%ebp)
	movl	$10,%eax
	pushl	%eax
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L43
.L42:
	movl	$_s0,%edx
	leal	-8(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	jmp	.L44
.L43:
	movl	$_s1,%edx
	leal	-8(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
.L44:
	pushl	-4(%ebp)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L46
.L45:
	call	_f
	jmp	.L47
.L46:
	call	_g
.L47:
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L49
.L48:
	movl	$_s0,%eax
	jmp	.L50
.L49:
	movl	$_s1,%eax
.L50:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L52
.L51:
	movl	$_s0,%eax
	jmp	.L53
.L52:
	movl	$0,%eax
.L53:
	cmpl	$0,%eax
	je	.L55
.L54:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	jmp	.L56
.L55:
	pushl	$_string.26
	call	_printf
	leal	4(%esp),%esp
.L56:
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L58
.L57:
	movl	$_s0,%eax
	jmp	.L59
.L58:
	movl	$_s1,%eax
.L59:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldz
	fchs
	fstpl	-24(%ebp)
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L61
.L60:
	movl	$10,%eax
	jmp	.L62
.L61:
	movl	$20,%eax
.L62:
	pushl	%eax
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L64
.L63:
	movl	$_s0,%edx
	leal	-16(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	jmp	.L65
.L64:
	movl	$_s1,%edx
	leal	-16(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
.L65:
	pushl	-12(%ebp)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L67
.L66:
	call	_f
	jmp	.L68
.L67:
	call	_g
.L68:
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L70
.L69:
	movl	$_s0,%eax
	jmp	.L71
.L70:
	movl	$_s1,%eax
.L71:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L73
.L72:
	movl	$_s0,%eax
	jmp	.L74
.L73:
	movl	$0,%eax
.L74:
	cmpl	$0,%eax
	je	.L76
.L75:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	jmp	.L77
.L76:
	pushl	$_string.26
	call	_printf
	leal	4(%esp),%esp
.L77:
	fldl	-24(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	and	$69,%ah
	cmp	$64,%ah
	je	.L79
.L78:
	movl	$_s0,%eax
	jmp	.L80
.L79:
	movl	$_s1,%eax
.L80:
	pushl	4(%eax)
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	fld1
	sub	$8,%esp
	fstpl	(%esp)
	call	_f0
	leal	8(%esp),%esp
	fldz
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f0
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_s0
_s0:
	.long	30
	.long	40
	.align	4
	.global	_s1
_s1:
	.long	50
	.long	60
