 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.23:
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
	pushl	$_string.23
	call	_printf
	leal	4(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.25:
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
	pushl	$_string.25
	call	_printf
	leal	4(%esp),%esp
.L6:
	leave
	ret

	.align	1
_string.27:
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.32:
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
	subl	$8,%esp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$0,%ebx
	je	.L10
.L9:
	movl	$10,%eax
	jmp	.L11
.L10:
	movl	$20,%eax
.L11:
	pushl	%eax
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
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
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
	je	.L16
.L15:
	call	_f
	jmp	.L17
.L16:
	call	_g
.L17:
	cmpl	$0,%ebx
	je	.L19
.L18:
	movl	$_s0,%eax
	jmp	.L20
.L19:
	movl	$_s1,%eax
.L20:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
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
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	jmp	.L26
.L25:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L26:
	cmpl	$0,%ebx
	je	.L28
.L27:
	movl	$_s0,%eax
	jmp	.L29
.L28:
	movl	$_s1,%eax
.L29:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
.L30:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$32,%esp
	pushl	%ebx
	movl	$_i,%eax
	cmpl	$0,%eax
	je	.L34
.L33:
	movl	$10,%eax
	jmp	.L35
.L34:
	movl	$20,%eax
.L35:
	pushl	%eax
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$_i,%eax
	cmpl	$0,%eax
	je	.L37
.L36:
	movl	$_s0,%edx
	leal	-8(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	jmp	.L38
.L37:
	movl	$_s1,%edx
	leal	-8(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
.L38:
	pushl	-4(%ebp)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$_i,%eax
	cmpl	$0,%eax
	je	.L40
.L39:
	call	_f
	jmp	.L41
.L40:
	call	_g
.L41:
	movl	$_i,%eax
	cmpl	$0,%eax
	je	.L43
.L42:
	movl	$_s0,%eax
	jmp	.L44
.L43:
	movl	$_s1,%eax
.L44:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$_i,%eax
	cmpl	$0,%eax
	je	.L46
.L45:
	movl	$_s0,%eax
	jmp	.L47
.L46:
	movl	$0,%eax
.L47:
	cmpl	$0,%eax
	je	.L49
.L48:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	jmp	.L50
.L49:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L50:
	movl	$_i,%eax
	cmpl	$0,%eax
	je	.L52
.L51:
	movl	$_s0,%eax
	jmp	.L53
.L52:
	movl	$_s1,%eax
.L53:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$20,%eax
	pushl	%eax
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$_s1,%edx
	leal	-16(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	pushl	-12(%ebp)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	call	_g
	movl	$_s1,%eax
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	cmpl	$0,%eax
	je	.L70
.L69:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	jmp	.L73
.L70:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L73:
	movl	$_s1,%eax
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$_i,%ebx
	cmpl	$0,%ebx
	je	.L76
.L75:
	movl	$10,%eax
	jmp	.L77
.L76:
	movl	$20,%eax
.L77:
	pushl	%eax
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
	je	.L79
.L78:
	movl	$_s0,%edx
	leal	-24(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	jmp	.L80
.L79:
	movl	$_s1,%edx
	leal	-24(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
.L80:
	pushl	-20(%ebp)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
	je	.L82
.L81:
	call	_f
	jmp	.L83
.L82:
	call	_g
.L83:
	cmpl	$0,%ebx
	je	.L85
.L84:
	movl	$_s0,%eax
	jmp	.L86
.L85:
	movl	$_s1,%eax
.L86:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
	je	.L88
.L87:
	movl	$_s0,%eax
	jmp	.L89
.L88:
	movl	$0,%eax
.L89:
	cmpl	$0,%eax
	je	.L91
.L90:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	jmp	.L92
.L91:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L92:
	cmpl	$0,%ebx
	je	.L94
.L93:
	movl	$_s0,%eax
	jmp	.L95
.L94:
	movl	$_s1,%eax
.L95:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%ebx
	movl	$20,%eax
	pushl	%eax
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
	je	.L100
.L99:
	movl	$_s0,%edx
	leal	-32(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	jmp	.L101
.L100:
	movl	$_s1,%edx
	leal	-32(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
.L101:
	pushl	-28(%ebp)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
	je	.L103
.L102:
	call	_f
	jmp	.L104
.L103:
	call	_g
.L104:
	cmpl	$0,%ebx
	je	.L106
.L105:
	movl	$_s0,%eax
	jmp	.L107
.L106:
	movl	$_s1,%eax
.L107:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	cmpl	$0,%ebx
	je	.L109
.L108:
	movl	$_s0,%eax
	jmp	.L110
.L109:
	movl	$0,%eax
.L110:
	cmpl	$0,%eax
	je	.L112
.L111:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	jmp	.L113
.L112:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L113:
	cmpl	$0,%ebx
	je	.L115
.L114:
	movl	$_s0,%eax
	jmp	.L116
.L115:
	movl	$_s1,%eax
.L116:
	pushl	4(%eax)
	pushl	$_string.27
	call	_printf
	leal	8(%esp),%esp
	pushl	$_i
	call	_f0
	leal	4(%esp),%esp
	pushl	$0
	call	_f0
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%ebx
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
	.comm	_i,4
