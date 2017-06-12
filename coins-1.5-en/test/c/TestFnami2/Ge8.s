 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.14:
	.byte	84
	.byte	0
	.align	1
_string.16:
	.byte	45
	.byte	0
	.align	1
_string.32:
	.byte	10
	.byte	0

	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L4
.L3:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L5
.L4:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L5:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L7
.L6:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L8
.L7:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L8:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L10
.L9:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L11
.L10:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L11:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L13
.L12:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L14
.L13:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L14:
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L16
.L15:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L17
.L16:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L17:
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L19
.L18:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L20
.L19:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L20:
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L22
.L21:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L23
.L22:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L23:
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L25
.L24:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L26
.L25:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L26:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L27:
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L31
.L30:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L32
.L31:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L32:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L34
.L33:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L35
.L34:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L35:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L37
.L36:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L38
.L37:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L38:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L40
.L39:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L41
.L40:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L41:
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L43
.L42:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L44
.L43:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L44:
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L46
.L45:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L47
.L46:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L47:
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L49
.L48:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L50
.L49:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L50:
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L52
.L51:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L53
.L52:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L53:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L54:
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L58
.L57:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L59
.L58:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L59:
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L61
.L60:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L62
.L61:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L62:
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L64
.L63:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L65
.L64:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L65:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L66:
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	fldl	.LC4
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L70
.L69:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L71
.L70:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L71:
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L73
.L72:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L74
.L73:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L74:
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L76
.L75:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L77
.L76:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L77:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L78:
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$16,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	fldl	16(%ebp)
	fstpl	-16(%ebp)
	fldl	-8(%ebp)
	fcompl	-16(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L82
.L81:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L83
.L82:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L83:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	fldl	.LC2
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	fldl	.LC3
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	fldz
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	fldz
	sub	$8,%esp
	fstpl	(%esp)
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	pushl	.LC3+4
	pushl	.LC3
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	pushl	.LC2+4
	pushl	.LC2
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	pushl	.LC1+4
	pushl	.LC1
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L86:
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	pushl	.LC4+4
	pushl	.LC4
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	call	_op
	leal	16(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L89:
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L93
.L92:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L94
.L93:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L94:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L96
.L95:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L97
.L96:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L97:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L99
.L98:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L100
.L99:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L100:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L102
.L101:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L103
.L102:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L103:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L105
.L104:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L106
.L105:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L106:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L108
.L107:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L109
.L108:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L109:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L111
.L110:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L112
.L111:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L112:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L114
.L113:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L115
.L114:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L115:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L117
.L116:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L118
.L117:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L118:
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L120
.L119:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L121
.L120:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L121:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L123
.L122:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L124
.L123:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L124:
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L126
.L125:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L127
.L126:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L127:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L129
.L128:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L130
.L129:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L130:
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L132
.L131:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L133
.L132:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L133:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L135
.L134:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L136
.L135:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L136:
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L138
.L137:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L139
.L138:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L139:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L141
.L140:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L142
.L141:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L142:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L144
.L143:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L145
.L144:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L145:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L147
.L146:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L148
.L147:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L148:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L150
.L149:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L151
.L150:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L151:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L153
.L152:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L154
.L153:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L154:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L156
.L155:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L157
.L156:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L157:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L159
.L158:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L160
.L159:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L160:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L162
.L161:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L163
.L162:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L163:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L165
.L164:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L166
.L165:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L166:
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L168
.L167:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L169
.L168:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L169:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L171
.L170:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L172
.L171:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L172:
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L174
.L173:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L175
.L174:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L175:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L177
.L176:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L178
.L177:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L178:
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L180
.L179:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L181
.L180:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L181:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L183
.L182:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L184
.L183:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L184:
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L186
.L185:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L187
.L186:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L187:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L189
.L188:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L190
.L189:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L190:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L192
.L191:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L193
.L192:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L193:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L195
.L194:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L196
.L195:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L196:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L198
.L197:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L199
.L198:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L199:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L201
.L200:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L202
.L201:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L202:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L204
.L203:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L205
.L204:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L205:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L207
.L206:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L208
.L207:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L208:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L210
.L209:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L211
.L210:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L211:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L213
.L212:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L214
.L213:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L214:
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L216
.L215:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L217
.L216:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L217:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L219
.L218:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L220
.L219:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L220:
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L222
.L221:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L223
.L222:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L223:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L225
.L224:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L226
.L225:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L226:
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L228
.L227:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L229
.L228:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L229:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L231
.L230:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L232
.L231:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L232:
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L234
.L233:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L235
.L234:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L235:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L237
.L236:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L238
.L237:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L238:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L240
.L239:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L241
.L240:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L241:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L243
.L242:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L244
.L243:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L244:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L246
.L245:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L247
.L246:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L247:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L249
.L248:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L250
.L249:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L250:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L252
.L251:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L253
.L252:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L253:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L255
.L254:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L256
.L255:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L256:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L258
.L257:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L259
.L258:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L259:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L261
.L260:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L262
.L261:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L262:
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L264
.L263:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L265
.L264:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L265:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L267
.L266:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L268
.L267:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L268:
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L270
.L269:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L271
.L270:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L271:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L273
.L272:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L274
.L273:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L274:
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L276
.L275:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L277
.L276:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L277:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L279
.L278:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L280
.L279:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L280:
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L282
.L281:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L283
.L282:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L283:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldz
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L285
.L284:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L286
.L285:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L286:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L288
.L287:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L289
.L288:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L289:
	fldz
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L291
.L290:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L292
.L291:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L292:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L294
.L293:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L295
.L294:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L295:
	fldz
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L297
.L296:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L298
.L297:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L298:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L300
.L299:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L301
.L300:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L301:
	fldz
	fstpl	-8(%ebp)
	fldz
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L303
.L302:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L304
.L303:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L304:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L306
.L305:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L307
.L306:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L307:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L312
.L311:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L313
.L312:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L313:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L318
.L317:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L319
.L318:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L319:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L324
.L323:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L325
.L324:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L325:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L330
.L329:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L331
.L330:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L331:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L333
.L332:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L334
.L333:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L334:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L336
.L335:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L337
.L336:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L337:
	fldl	.LC3
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L339
.L338:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L340
.L339:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L340:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L342
.L341:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L343
.L342:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L343:
	fldl	.LC3
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L345
.L344:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L346
.L345:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L346:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L348
.L347:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L349
.L348:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L349:
	fldl	.LC3
	fstpl	-8(%ebp)
	fldz
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L351
.L350:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L352
.L351:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L352:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L354
.L353:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L355
.L354:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L355:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L360
.L359:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L361
.L360:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L361:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L366
.L365:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L367
.L366:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L367:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L372
.L371:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L373
.L372:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L373:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L378
.L377:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L379
.L378:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L379:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L381
.L380:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L382
.L381:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L382:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L384
.L383:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L385
.L384:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L385:
	fldl	.LC2
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L387
.L386:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L388
.L387:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L388:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L390
.L389:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L391
.L390:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L391:
	fldl	.LC2
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L393
.L392:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L394
.L393:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L394:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L396
.L395:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L397
.L396:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L397:
	fldl	.LC2
	fstpl	-8(%ebp)
	fldz
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L399
.L398:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L400
.L399:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L400:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L402
.L401:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L403
.L402:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L403:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L408
.L407:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L409
.L408:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L409:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L414
.L413:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L415
.L414:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L415:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L420
.L419:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L421
.L420:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L421:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L426
.L425:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L427
.L426:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L427:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L429
.L428:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L430
.L429:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L430:
	fldl	.LC1
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L432
.L431:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L433
.L432:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L433:
	fldl	.LC1
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L435
.L434:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L436
.L435:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L436:
	fldl	.LC2
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L438
.L437:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L439
.L438:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L439:
	fldl	.LC1
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L441
.L440:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L442
.L441:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L442:
	fldl	.LC3
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L444
.L443:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L445
.L444:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L445:
	fldl	.LC1
	fstpl	-8(%ebp)
	fldz
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L447
.L446:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L448
.L447:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L448:
	fldz
	fchs
	fldl	-8(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L450
.L449:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L451
.L450:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L451:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L456
.L455:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L457
.L456:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L457:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L462
.L461:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L463
.L462:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L463:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L468
.L467:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L469
.L468:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L469:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L474
.L473:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L475
.L474:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L475:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L476:
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	.LC4
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L483
.L482:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L484
.L483:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L484:
	fldl	.LC4
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L489
.L488:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L490
.L489:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L490:
	fldl	.LC4
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L495
.L494:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L496
.L495:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L496:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L501
.L500:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L502
.L501:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L502:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L507
.L506:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L508
.L507:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L508:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L513
.L512:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L514
.L513:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L514:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L519
.L518:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L520
.L519:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L520:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L525
.L524:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L526
.L525:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L526:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	-8(%ebp)
	fcompl	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L531
.L530:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L532
.L531:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L532:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L533:
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L537
.L536:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L538
.L537:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L538:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L540
.L539:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L541
.L540:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L541:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L543
.L542:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L544
.L543:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L544:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L546
.L545:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L547
.L546:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L547:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L549
.L548:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L550
.L549:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L550:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L552
.L551:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L553
.L552:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L553:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L555
.L554:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L556
.L555:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L556:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L558
.L557:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L559
.L558:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L559:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L561
.L560:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L562
.L561:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L562:
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L564
.L563:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L565
.L564:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L565:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L567
.L566:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L568
.L567:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L568:
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L570
.L569:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L571
.L570:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L571:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L573
.L572:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L574
.L573:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L574:
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L576
.L575:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L577
.L576:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L577:
	fldl	.LC1
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L579
.L578:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L580
.L579:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L580:
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L582
.L581:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L583
.L582:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L583:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L585
.L584:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L586
.L585:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L586:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L588
.L587:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L589
.L588:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L589:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L591
.L590:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L592
.L591:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L592:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L594
.L593:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L595
.L594:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L595:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L597
.L596:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L598
.L597:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L598:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L600
.L599:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L601
.L600:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L601:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L603
.L602:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L604
.L603:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L604:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L606
.L605:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L607
.L606:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L607:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L609
.L608:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L610
.L609:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L610:
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L612
.L611:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L613
.L612:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L613:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L615
.L614:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L616
.L615:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L616:
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L618
.L617:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L619
.L618:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L619:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L621
.L620:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L622
.L621:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L622:
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L624
.L623:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L625
.L624:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L625:
	fldl	.LC2
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L627
.L626:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L628
.L627:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L628:
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L630
.L629:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L631
.L630:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L631:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L633
.L632:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L634
.L633:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L634:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L636
.L635:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L637
.L636:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L637:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L639
.L638:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L640
.L639:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L640:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L642
.L641:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L643
.L642:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L643:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L645
.L644:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L646
.L645:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L646:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L648
.L647:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L649
.L648:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L649:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L651
.L650:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L652
.L651:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L652:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L654
.L653:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L655
.L654:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L655:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L657
.L656:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L658
.L657:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L658:
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L660
.L659:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L661
.L660:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L661:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L663
.L662:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L664
.L663:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L664:
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L666
.L665:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L667
.L666:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L667:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L669
.L668:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L670
.L669:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L670:
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L672
.L671:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L673
.L672:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L673:
	fldl	.LC3
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L675
.L674:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L676
.L675:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L676:
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L678
.L677:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L679
.L678:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L679:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L681
.L680:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L682
.L681:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L682:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L684
.L683:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L685
.L684:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L685:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L687
.L686:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L688
.L687:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L688:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L690
.L689:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L691
.L690:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L691:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L693
.L692:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L694
.L693:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L694:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L696
.L695:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L697
.L696:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L697:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L699
.L698:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L700
.L699:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L700:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L702
.L701:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L703
.L702:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L703:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L705
.L704:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L706
.L705:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L706:
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L708
.L707:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L709
.L708:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L709:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L711
.L710:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L712
.L711:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L712:
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L714
.L713:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L715
.L714:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L715:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L717
.L716:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L718
.L717:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L718:
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L720
.L719:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L721
.L720:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L721:
	fldz
	fchs
	fstpl	-8(%ebp)
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L723
.L722:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L724
.L723:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L724:
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L726
.L725:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L727
.L726:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L727:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldz
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L729
.L728:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L730
.L729:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L730:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L732
.L731:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L733
.L732:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L733:
	fldz
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L735
.L734:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L736
.L735:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L736:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L738
.L737:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L739
.L738:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L739:
	fldz
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L741
.L740:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L742
.L741:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L742:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L744
.L743:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L745
.L744:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L745:
	fldz
	fstpl	-8(%ebp)
	fldz
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L747
.L746:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L748
.L747:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L748:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L750
.L749:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L751
.L750:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L751:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L756
.L755:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L757
.L756:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L757:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L762
.L761:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L763
.L762:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L763:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L768
.L767:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L769
.L768:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L769:
	fldz
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L774
.L773:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L775
.L774:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L775:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L777
.L776:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L778
.L777:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L778:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L780
.L779:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L781
.L780:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L781:
	fldl	.LC3
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L783
.L782:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L784
.L783:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L784:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L786
.L785:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L787
.L786:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L787:
	fldl	.LC3
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L789
.L788:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L790
.L789:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L790:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L792
.L791:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L793
.L792:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L793:
	fldl	.LC3
	fstpl	-8(%ebp)
	fldz
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L795
.L794:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L796
.L795:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L796:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L798
.L797:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L799
.L798:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L799:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L804
.L803:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L805
.L804:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L805:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L810
.L809:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L811
.L810:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L811:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L816
.L815:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L817
.L816:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L817:
	fldl	.LC3
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L822
.L821:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L823
.L822:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L823:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L825
.L824:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L826
.L825:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L826:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L828
.L827:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L829
.L828:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L829:
	fldl	.LC2
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L831
.L830:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L832
.L831:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L832:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L834
.L833:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L835
.L834:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L835:
	fldl	.LC2
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L837
.L836:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L838
.L837:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L838:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L840
.L839:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L841
.L840:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L841:
	fldl	.LC2
	fstpl	-8(%ebp)
	fldz
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L843
.L842:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L844
.L843:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L844:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L846
.L845:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L847
.L846:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L847:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L852
.L851:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L853
.L852:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L853:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L858
.L857:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L859
.L858:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L859:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L864
.L863:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L865
.L864:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L865:
	fldl	.LC2
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L870
.L869:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L871
.L870:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L871:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fstpl	-8(%ebp)
	fldl	.LC1
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L873
.L872:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L874
.L873:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L874:
	fldl	.LC1
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L876
.L875:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L877
.L876:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L877:
	fldl	.LC1
	fstpl	-8(%ebp)
	fldl	.LC2
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L879
.L878:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L880
.L879:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L880:
	fldl	.LC2
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L882
.L881:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L883
.L882:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L883:
	fldl	.LC1
	fstpl	-8(%ebp)
	fldl	.LC3
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L885
.L884:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L886
.L885:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L886:
	fldl	.LC3
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L888
.L887:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L889
.L888:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L889:
	fldl	.LC1
	fstpl	-8(%ebp)
	fldz
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L891
.L890:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L892
.L891:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L892:
	fldz
	fchs
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L894
.L893:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L895
.L894:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L895:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldz
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L900
.L899:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L901
.L900:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L901:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L906
.L905:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L907
.L906:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L907:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L912
.L911:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L913
.L912:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L913:
	fldl	.LC1
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L918
.L917:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L919
.L918:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L919:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L920:
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	.LC4
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC4
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L927
.L926:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L928
.L927:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L928:
	fldl	.LC4
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L933
.L932:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L934
.L933:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L934:
	fldl	.LC4
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L939
.L938:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L940
.L939:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L940:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC4
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L945
.L944:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L946
.L945:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L946:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L951
.L950:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L952
.L951:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L952:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L957
.L956:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L958
.L957:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L958:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC4
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L963
.L962:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L964
.L963:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L964:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L969
.L968:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L970
.L969:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L970:
	fldl	.LC5
	fstpl	-8(%ebp)
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC5
	fcompl	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L975
.L974:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L976
.L975:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L976:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
.L977:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	fldl	.LC1
	fchs
	fldl	.LC1
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L981
.L980:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L982
.L981:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L982:
	fldl	.LC1
	fchs
	fldl	.LC2
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L984
.L983:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L985
.L984:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L985:
	fldl	.LC1
	fchs
	fldl	.LC3
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L987
.L986:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L988
.L987:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L988:
	fldl	.LC1
	fchs
	fldz
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L990
.L989:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L991
.L990:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L991:
	fldl	.LC1
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L993
.L992:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L994
.L993:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L994:
	fldl	.LC1
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L996
.L995:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L997
.L996:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L997:
	fldl	.LC1
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L999
.L998:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1000
.L999:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1000:
	fldl	.LC1
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1002
.L1001:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1003
.L1002:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1003:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC2
	fchs
	fldl	.LC1
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1005
.L1004:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1006
.L1005:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1006:
	fldl	.LC2
	fchs
	fldl	.LC2
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1008
.L1007:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1009
.L1008:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1009:
	fldl	.LC2
	fchs
	fldl	.LC3
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1011
.L1010:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1012
.L1011:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1012:
	fldl	.LC2
	fchs
	fldz
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1014
.L1013:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1015
.L1014:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1015:
	fldl	.LC2
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1017
.L1016:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1018
.L1017:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1018:
	fldl	.LC2
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1020
.L1019:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1021
.L1020:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1021:
	fldl	.LC2
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1023
.L1022:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1024
.L1023:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1024:
	fldl	.LC2
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1026
.L1025:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1027
.L1026:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1027:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC3
	fchs
	fldl	.LC1
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1029
.L1028:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1030
.L1029:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1030:
	fldl	.LC3
	fchs
	fldl	.LC2
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1032
.L1031:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1033
.L1032:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1033:
	fldl	.LC3
	fchs
	fldl	.LC3
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1035
.L1034:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1036
.L1035:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1036:
	fldl	.LC3
	fchs
	fldz
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1038
.L1037:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1039
.L1038:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1039:
	fldl	.LC3
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1041
.L1040:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1042
.L1041:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1042:
	fldl	.LC3
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1044
.L1043:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1045
.L1044:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1045:
	fldl	.LC3
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1047
.L1046:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1048
.L1047:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1048:
	fldl	.LC3
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1050
.L1049:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1051
.L1050:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1051:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldz
	fchs
	fldl	.LC1
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1053
.L1052:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1054
.L1053:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1054:
	fldz
	fchs
	fldl	.LC2
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1056
.L1055:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1057
.L1056:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1057:
	fldz
	fchs
	fldl	.LC3
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1059
.L1058:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1060
.L1059:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1060:
	fldz
	fchs
	fldz
	fchs
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1062
.L1061:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1063
.L1062:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1063:
	fldz
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1065
.L1064:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1066
.L1065:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1066:
	fldz
	fchs
	fcompl	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1068
.L1067:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1069
.L1068:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1069:
	fldz
	fchs
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1071
.L1070:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1072
.L1071:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1072:
	fldz
	fchs
	fcompl	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1074
.L1073:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1075
.L1074:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1075:
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1077
.L1076:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1078
.L1077:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1078:
	fldl	.LC2
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1080
.L1079:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1081
.L1080:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1081:
	fldl	.LC3
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1083
.L1082:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1084
.L1083:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1084:
	fldz
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1086
.L1085:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1088
.L1086:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1088:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1101
.L1100:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1102
.L1101:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1102:
	fldl	.LC2
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1104
.L1103:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1105
.L1104:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1105:
	fldl	.LC3
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1107
.L1106:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1108
.L1107:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1108:
	fldz
	fchs
	fldl	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1110
.L1109:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1112
.L1110:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1112:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1125
.L1124:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1126
.L1125:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1126:
	fldl	.LC2
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1128
.L1127:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1129
.L1128:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1129:
	fldl	.LC3
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1131
.L1130:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1132
.L1131:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1132:
	fldz
	fchs
	fldl	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1134
.L1133:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1136
.L1134:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1136:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	fldl	.LC1
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1149
.L1148:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1150
.L1149:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1150:
	fldl	.LC2
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1152
.L1151:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1153
.L1152:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1153:
	fldl	.LC3
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1155
.L1154:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1156
.L1155:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1156:
	fldz
	fchs
	fldl	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L1158
.L1157:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L1160
.L1158:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L1160:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.32
	call	_printf
	leal	4(%esp),%esp
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	fldl	.LC1
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f0
	leal	8(%esp),%esp
	fldl	.LC2
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f0
	leal	8(%esp),%esp
	fldl	.LC3
	fchs
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
	fldz
	sub	$8,%esp
	fstpl	(%esp)
	call	_f0
	leal	8(%esp),%esp
	pushl	.LC3+4
	pushl	.LC3
	call	_f0
	leal	8(%esp),%esp
	pushl	.LC2+4
	pushl	.LC2
	call	_f0
	leal	8(%esp),%esp
	pushl	.LC1+4
	pushl	.LC1
	call	_f0
	leal	8(%esp),%esp
	pushl	.LC4+4
	pushl	.LC4
	call	_g0
	leal	8(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	call	_g0
	leal	8(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	call	_g0
	leal	8(%esp),%esp
	fldl	.LC1
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f1
	leal	8(%esp),%esp
	fldl	.LC2
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f1
	leal	8(%esp),%esp
	fldl	.LC3
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f1
	leal	8(%esp),%esp
	fldz
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f1
	leal	8(%esp),%esp
	fldz
	sub	$8,%esp
	fstpl	(%esp)
	call	_f1
	leal	8(%esp),%esp
	pushl	.LC3+4
	pushl	.LC3
	call	_f1
	leal	8(%esp),%esp
	pushl	.LC2+4
	pushl	.LC2
	call	_f1
	leal	8(%esp),%esp
	pushl	.LC1+4
	pushl	.LC1
	call	_f1
	leal	8(%esp),%esp
	pushl	.LC4+4
	pushl	.LC4
	call	_g1
	leal	8(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	call	_g1
	leal	8(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	call	_g1
	leal	8(%esp),%esp
	fldl	.LC1
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f2
	leal	8(%esp),%esp
	fldl	.LC2
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f2
	leal	8(%esp),%esp
	fldl	.LC3
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f2
	leal	8(%esp),%esp
	fldz
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	call	_f2
	leal	8(%esp),%esp
	fldz
	sub	$8,%esp
	fstpl	(%esp)
	call	_f2
	leal	8(%esp),%esp
	pushl	.LC3+4
	pushl	.LC3
	call	_f2
	leal	8(%esp),%esp
	pushl	.LC2+4
	pushl	.LC2
	call	_f2
	leal	8(%esp),%esp
	pushl	.LC1+4
	pushl	.LC1
	call	_f2
	leal	8(%esp),%esp
	pushl	.LC4+4
	pushl	.LC4
	call	_g2
	leal	8(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	call	_g2
	leal	8(%esp),%esp
	pushl	.LC5+4
	pushl	.LC5
	call	_g2
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.align	4
.LC1:
	.long	0xffffffff,0x7fefffff /* 1.7976931348623157E308 */
	.align	4
.LC2:
	.long	0x9999999a,0x40059999 /* 2.7 */
	.align	4
.LC3:
	.long	0x0,0x100000 /* 2.2250738585072014E-308 */
	.align	4
.LC4:
	.long	0x8b14575e,0x4005bf0a /* 2.71828182845904 */
	.align	4
.LC5:
	.long	0x8b145769,0x4005bf0a /* 2.718281828459045 */
