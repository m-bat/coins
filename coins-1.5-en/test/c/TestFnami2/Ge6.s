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
_string.28:
	.byte	10
	.byte	0

	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$0,%ebx
	jb	.L4
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
	cmpl	$1,%ebx
	jb	.L7
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
	cmpl	$19,%ebx
	jb	.L10
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
	cmpl	$20,%ebx
	jb	.L13
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
	cmpl	$-2147483638,%ebx
	jb	.L16
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
	cmpl	$-2147483637,%ebx
	jb	.L19
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L21:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$0,%ebx
	ja	.L25
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
	cmpl	$1,%ebx
	ja	.L28
.L27:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L29
.L28:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L29:
	cmpl	$19,%ebx
	ja	.L31
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
	cmpl	$20,%ebx
	ja	.L34
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
	cmpl	$-2147483638,%ebx
	ja	.L37
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
	cmpl	$-2147483637,%ebx
	ja	.L40
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L42:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$0,%ebx
	jb	.L46
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
	cmpl	$-2,%ebx
	jb	.L49
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
	cmpl	$-1,%ebx
	jb	.L52
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L54:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$0,%ebx
	ja	.L58
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
	cmpl	$-2,%ebx
	ja	.L61
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
	cmpl	$-1,%ebx
	ja	.L64
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L66:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	jb	.L70
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
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$0
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$1
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$19
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$20
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$-2147483638
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$-2147483637
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L74:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$0
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$-2
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$-1
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L77:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L84
.L83:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L85
.L84:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L85:
	movl	$0,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	jb	.L90
.L89:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L91
.L90:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L91:
	movl	$0,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	jb	.L96
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
	movl	$0,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	jb	.L102
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
	movl	$0,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	jb	.L108
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
	movl	$0,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	jb	.L114
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L120
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
	movl	$1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	jb	.L126
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
	movl	$1,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	jb	.L132
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
	movl	$1,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	jb	.L138
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
	movl	$1,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	jb	.L144
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
	movl	$1,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	jb	.L150
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$19,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L156
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
	movl	$19,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	jb	.L162
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
	movl	$19,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	jb	.L168
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
	movl	$19,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	jb	.L174
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
	movl	$19,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	jb	.L180
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
	movl	$19,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	jb	.L186
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$20,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L192
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
	movl	$20,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	jb	.L198
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
	movl	$20,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	jb	.L204
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
	movl	$20,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	jb	.L210
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
	movl	$20,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	jb	.L216
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
	movl	$20,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	jb	.L222
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-2147483638,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L228
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
	movl	$-2147483638,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	jb	.L234
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
	movl	$-2147483638,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	jb	.L240
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
	movl	$-2147483638,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	jb	.L246
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
	movl	$-2147483638,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	jb	.L252
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
	movl	$-2147483638,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	jb	.L258
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-2147483637,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L264
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
	movl	$-2147483637,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	jb	.L270
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
	movl	$-2147483637,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	jb	.L276
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
	movl	$-2147483637,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	jb	.L282
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
	movl	$-2147483637,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	jb	.L288
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
	movl	$-2147483637,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	jb	.L294
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L296:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L303
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
	movl	$0,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2,%ebx
	jb	.L309
.L308:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L310
.L309:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L310:
	movl	$0,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-1,%ebx
	jb	.L315
.L314:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L316
.L315:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L316:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-2,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L321
.L320:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L322
.L321:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L322:
	movl	$-2,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2,%ebx
	jb	.L327
.L326:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L328
.L327:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L328:
	movl	$-2,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-1,%ebx
	jb	.L333
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	jb	.L339
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
	movl	$-1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2,%ebx
	jb	.L345
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
	movl	$-1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-1,%ebx
	jb	.L351
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L353:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L360
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
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	ja	.L366
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
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	ja	.L372
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
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	ja	.L378
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
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	ja	.L384
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
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	ja	.L390
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$1,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L396
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
	movl	$1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	ja	.L402
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
	movl	$1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	ja	.L408
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
	movl	$1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	ja	.L414
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
	movl	$1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	ja	.L420
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
	movl	$1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	ja	.L426
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$19,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L432
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
	movl	$19,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	ja	.L438
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
	movl	$19,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	ja	.L444
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
	movl	$19,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	ja	.L450
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
	movl	$19,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	ja	.L456
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
	movl	$19,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	ja	.L462
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$20,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L468
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
	movl	$20,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	ja	.L474
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
	movl	$20,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	ja	.L480
.L479:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L481
.L480:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L481:
	movl	$20,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	ja	.L486
.L485:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L487
.L486:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L487:
	movl	$20,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	ja	.L492
.L491:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L493
.L492:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L493:
	movl	$20,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	ja	.L498
.L497:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L499
.L498:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L499:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-2147483638,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L504
.L503:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L505
.L504:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L505:
	movl	$-2147483638,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	ja	.L510
.L509:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L511
.L510:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L511:
	movl	$-2147483638,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	ja	.L516
.L515:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L517
.L516:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L517:
	movl	$-2147483638,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	ja	.L522
.L521:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L523
.L522:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L523:
	movl	$-2147483638,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	ja	.L528
.L527:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L529
.L528:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L529:
	movl	$-2147483638,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	ja	.L534
.L533:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	jmp	.L535
.L534:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L535:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-2147483637,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L540
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
	movl	$-2147483637,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$1,%ebx
	ja	.L546
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
	movl	$-2147483637,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$19,%ebx
	ja	.L552
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
	movl	$-2147483637,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$20,%ebx
	ja	.L558
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
	movl	$-2147483637,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483638,%ebx
	ja	.L564
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
	movl	$-2147483637,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2147483637,%ebx
	ja	.L570
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L572:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L579
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
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2,%ebx
	ja	.L585
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
	movl	$0,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-1,%ebx
	ja	.L591
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-2,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L597
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
	movl	$-2,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2,%ebx
	ja	.L603
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
	movl	$-2,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-1,%ebx
	ja	.L609
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$-1,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	ja	.L615
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
	movl	$-1,%ebx
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-2,%ebx
	ja	.L621
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
	movl	$-1,%ebx
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	cmpl	$-1,%ebx
	ja	.L627
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L629:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
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
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.28
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
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.28
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
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.28
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
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.28
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
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.28
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
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.28
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
	pushl	$_string.28
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
	pushl	$_string.28
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
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	pushl	$0
	call	_f0
	leal	4(%esp),%esp
	pushl	$1
	call	_f0
	leal	4(%esp),%esp
	pushl	$19
	call	_f0
	leal	4(%esp),%esp
	pushl	$20
	call	_f0
	leal	4(%esp),%esp
	pushl	$-2147483638
	call	_f0
	leal	4(%esp),%esp
	pushl	$-2147483637
	call	_f0
	leal	4(%esp),%esp
	pushl	$0
	call	_g0
	leal	4(%esp),%esp
	pushl	$-2
	call	_g0
	leal	4(%esp),%esp
	pushl	$-1
	call	_g0
	leal	4(%esp),%esp
	pushl	$0
	call	_f1
	leal	4(%esp),%esp
	pushl	$1
	call	_f1
	leal	4(%esp),%esp
	pushl	$19
	call	_f1
	leal	4(%esp),%esp
	pushl	$20
	call	_f1
	leal	4(%esp),%esp
	pushl	$-2147483638
	call	_f1
	leal	4(%esp),%esp
	pushl	$-2147483637
	call	_f1
	leal	4(%esp),%esp
	pushl	$0
	call	_g1
	leal	4(%esp),%esp
	pushl	$-2
	call	_g1
	leal	4(%esp),%esp
	pushl	$-1
	call	_g1
	leal	4(%esp),%esp
	pushl	$0
	call	_f2
	leal	4(%esp),%esp
	pushl	$1
	call	_f2
	leal	4(%esp),%esp
	pushl	$19
	call	_f2
	leal	4(%esp),%esp
	pushl	$20
	call	_f2
	leal	4(%esp),%esp
	pushl	$-2147483638
	call	_f2
	leal	4(%esp),%esp
	pushl	$-2147483637
	call	_f2
	leal	4(%esp),%esp
	pushl	$0
	call	_g2
	leal	4(%esp),%esp
	pushl	$-2
	call	_g2
	leal	4(%esp),%esp
	pushl	$-1
	call	_g2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

