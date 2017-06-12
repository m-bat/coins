 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	cmpl	$0,%eax
	jae	.L4
.L3:
	movl	$1,%edi
	jmp	.L5
.L4:
	movl	$0,%edi
.L5:
	cmpl	$1,%eax
	jae	.L7
.L6:
	movl	$1,%esi
	jmp	.L8
.L7:
	movl	$0,%esi
.L8:
	cmpl	$19,%eax
	jae	.L10
.L9:
	movl	$1,%ebx
	jmp	.L11
.L10:
	movl	$0,%ebx
.L11:
	cmpl	$20,%eax
	jae	.L13
.L12:
	movl	$1,%edx
	jmp	.L14
.L13:
	movl	$0,%edx
.L14:
	cmpl	$-2147483638,%eax
	jae	.L16
.L15:
	movl	$1,%ecx
	jmp	.L17
.L16:
	movl	$0,%ecx
.L17:
	cmpl	$-2147483637,%eax
	jae	.L19
.L18:
	movl	$1,%eax
	jmp	.L20
.L19:
	movl	$0,%eax
.L20:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
.L21:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	cmpl	$0,%eax
	jbe	.L25
.L24:
	movl	$1,%edi
	jmp	.L26
.L25:
	movl	$0,%edi
.L26:
	cmpl	$1,%eax
	jbe	.L28
.L27:
	movl	$1,%esi
	jmp	.L29
.L28:
	movl	$0,%esi
.L29:
	cmpl	$19,%eax
	jbe	.L31
.L30:
	movl	$1,%ebx
	jmp	.L32
.L31:
	movl	$0,%ebx
.L32:
	cmpl	$20,%eax
	jbe	.L34
.L33:
	movl	$1,%edx
	jmp	.L35
.L34:
	movl	$0,%edx
.L35:
	cmpl	$-2147483638,%eax
	jbe	.L37
.L36:
	movl	$1,%ecx
	jmp	.L38
.L37:
	movl	$0,%ecx
.L38:
	cmpl	$-2147483637,%eax
	jbe	.L40
.L39:
	movl	$1,%eax
	jmp	.L41
.L40:
	movl	$0,%eax
.L41:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
.L42:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$0,%eax
	jae	.L46
.L45:
	movl	$1,%edx
	jmp	.L47
.L46:
	movl	$0,%edx
.L47:
	cmpl	$-2,%eax
	jae	.L49
.L48:
	movl	$1,%ecx
	jmp	.L50
.L49:
	movl	$0,%ecx
.L50:
	cmpl	$-1,%eax
	jae	.L52
.L51:
	movl	$1,%eax
	jmp	.L53
.L52:
	movl	$0,%eax
.L53:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L54:
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$0,%eax
	jbe	.L58
.L57:
	movl	$1,%edx
	jmp	.L59
.L58:
	movl	$0,%edx
.L59:
	cmpl	$-2,%eax
	jbe	.L61
.L60:
	movl	$1,%ecx
	jmp	.L62
.L61:
	movl	$0,%ecx
.L62:
	cmpl	$-1,%eax
	jbe	.L64
.L63:
	movl	$1,%eax
	jmp	.L65
.L64:
	movl	$0,%eax
.L65:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L66:
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
	jae	.L70
.L69:
	movl	$1,%eax
	jmp	.L71
.L70:
	movl	$0,%eax
.L71:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$0
	pushl	%edi
	call	_op
	movl	%eax,-12(%ebp)
	leal	8(%esp),%esp
	pushl	$1
	pushl	%edi
	call	_op
	movl	%eax,-8(%ebp)
	leal	8(%esp),%esp
	pushl	$19
	pushl	%edi
	call	_op
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$20
	pushl	%edi
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$-2147483638
	pushl	%edi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$-2147483637
	pushl	%edi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	-4(%ebp)
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
.L75:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$0
	pushl	%edi
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$-2
	pushl	%edi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$-1
	pushl	%edi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L78:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,%ebx
	movl	$0,-24(%ebp)
	cmpl	$1,%ebx
	jae	.L85
.L84:
	movl	$1,%edi
	jmp	.L86
.L85:
	movl	$0,%edi
.L86:
	cmpl	$19,%ebx
	jae	.L88
.L87:
	movl	$1,%esi
	jmp	.L89
.L88:
	movl	$0,%esi
.L89:
	cmpl	$20,%ebx
	jae	.L91
.L90:
	movl	$1,%edx
	jmp	.L92
.L91:
	movl	$0,%edx
.L92:
	cmpl	$-2147483638,%ebx
	jae	.L94
.L93:
	movl	$1,%ecx
	jmp	.L95
.L94:
	movl	$0,%ecx
.L95:
	cmpl	$-2147483637,%ebx
	jae	.L97
.L96:
	movl	$1,%eax
	jmp	.L98
.L97:
	movl	$0,%eax
.L98:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-24(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jae	.L100
.L99:
	movl	$1,%edi
	jmp	.L101
.L100:
	movl	$0,%edi
.L101:
	cmpl	$1,%ebx
	jae	.L103
.L102:
	movl	$1,%esi
	jmp	.L104
.L103:
	movl	$0,%esi
.L104:
	cmpl	$19,%ebx
	jae	.L106
.L105:
	movl	$1,%edx
	jmp	.L107
.L106:
	movl	$0,%edx
.L107:
	cmpl	$20,%ebx
	jae	.L109
.L108:
	movl	$1,%ecx
	jmp	.L110
.L109:
	movl	$0,%ecx
.L110:
	cmpl	$-2147483638,%ebx
	jae	.L112
.L111:
	movl	$1,%eax
	jmp	.L113
.L112:
	movl	$0,%eax
.L113:
	cmpl	$-2147483637,%ebx
	jae	.L115
.L114:
	movl	$1,%ebx
	jmp	.L116
.L115:
	movl	$0,%ebx
.L116:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$1,%ebx
	movl	$0,-20(%ebp)
	cmpl	$1,%ebx
	jae	.L121
.L120:
	movl	$1,%edi
	jmp	.L122
.L121:
	movl	$0,%edi
.L122:
	cmpl	$19,%ebx
	jae	.L124
.L123:
	movl	$1,%esi
	jmp	.L125
.L124:
	movl	$0,%esi
.L125:
	cmpl	$20,%ebx
	jae	.L127
.L126:
	movl	$1,%edx
	jmp	.L128
.L127:
	movl	$0,%edx
.L128:
	cmpl	$-2147483638,%ebx
	jae	.L130
.L129:
	movl	$1,%ecx
	jmp	.L131
.L130:
	movl	$0,%ecx
.L131:
	cmpl	$-2147483637,%ebx
	jae	.L133
.L132:
	movl	$1,%eax
	jmp	.L134
.L133:
	movl	$0,%eax
.L134:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-20(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jae	.L136
.L135:
	movl	$1,%edi
	jmp	.L137
.L136:
	movl	$0,%edi
.L137:
	cmpl	$1,%ebx
	jae	.L139
.L138:
	movl	$1,%esi
	jmp	.L140
.L139:
	movl	$0,%esi
.L140:
	cmpl	$19,%ebx
	jae	.L142
.L141:
	movl	$1,%edx
	jmp	.L143
.L142:
	movl	$0,%edx
.L143:
	cmpl	$20,%ebx
	jae	.L145
.L144:
	movl	$1,%ecx
	jmp	.L146
.L145:
	movl	$0,%ecx
.L146:
	cmpl	$-2147483638,%ebx
	jae	.L148
.L147:
	movl	$1,%eax
	jmp	.L149
.L148:
	movl	$0,%eax
.L149:
	cmpl	$-2147483637,%ebx
	jae	.L151
.L150:
	movl	$1,%ebx
	jmp	.L152
.L151:
	movl	$0,%ebx
.L152:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$19,%ebx
	movl	$0,-16(%ebp)
	cmpl	$1,%ebx
	jae	.L157
.L156:
	movl	$1,%edi
	jmp	.L158
.L157:
	movl	$0,%edi
.L158:
	cmpl	$19,%ebx
	jae	.L160
.L159:
	movl	$1,%esi
	jmp	.L161
.L160:
	movl	$0,%esi
.L161:
	cmpl	$20,%ebx
	jae	.L163
.L162:
	movl	$1,%edx
	jmp	.L164
.L163:
	movl	$0,%edx
.L164:
	cmpl	$-2147483638,%ebx
	jae	.L166
.L165:
	movl	$1,%ecx
	jmp	.L167
.L166:
	movl	$0,%ecx
.L167:
	cmpl	$-2147483637,%ebx
	jae	.L169
.L168:
	movl	$1,%eax
	jmp	.L170
.L169:
	movl	$0,%eax
.L170:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-16(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jae	.L172
.L171:
	movl	$1,%edi
	jmp	.L173
.L172:
	movl	$0,%edi
.L173:
	cmpl	$1,%ebx
	jae	.L175
.L174:
	movl	$1,%esi
	jmp	.L176
.L175:
	movl	$0,%esi
.L176:
	cmpl	$19,%ebx
	jae	.L178
.L177:
	movl	$1,%edx
	jmp	.L179
.L178:
	movl	$0,%edx
.L179:
	cmpl	$20,%ebx
	jae	.L181
.L180:
	movl	$1,%ecx
	jmp	.L182
.L181:
	movl	$0,%ecx
.L182:
	cmpl	$-2147483638,%ebx
	jae	.L184
.L183:
	movl	$1,%eax
	jmp	.L185
.L184:
	movl	$0,%eax
.L185:
	cmpl	$-2147483637,%ebx
	jae	.L187
.L186:
	movl	$1,%ebx
	jmp	.L188
.L187:
	movl	$0,%ebx
.L188:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$20,%ebx
	movl	$0,-12(%ebp)
	cmpl	$1,%ebx
	jae	.L193
.L192:
	movl	$1,%edi
	jmp	.L194
.L193:
	movl	$0,%edi
.L194:
	cmpl	$19,%ebx
	jae	.L196
.L195:
	movl	$1,%esi
	jmp	.L197
.L196:
	movl	$0,%esi
.L197:
	cmpl	$20,%ebx
	jae	.L199
.L198:
	movl	$1,%edx
	jmp	.L200
.L199:
	movl	$0,%edx
.L200:
	cmpl	$-2147483638,%ebx
	jae	.L202
.L201:
	movl	$1,%ecx
	jmp	.L203
.L202:
	movl	$0,%ecx
.L203:
	cmpl	$-2147483637,%ebx
	jae	.L205
.L204:
	movl	$1,%eax
	jmp	.L206
.L205:
	movl	$0,%eax
.L206:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-12(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jae	.L208
.L207:
	movl	$1,%edi
	jmp	.L209
.L208:
	movl	$0,%edi
.L209:
	cmpl	$1,%ebx
	jae	.L211
.L210:
	movl	$1,%esi
	jmp	.L212
.L211:
	movl	$0,%esi
.L212:
	cmpl	$19,%ebx
	jae	.L214
.L213:
	movl	$1,%edx
	jmp	.L215
.L214:
	movl	$0,%edx
.L215:
	cmpl	$20,%ebx
	jae	.L217
.L216:
	movl	$1,%ecx
	jmp	.L218
.L217:
	movl	$0,%ecx
.L218:
	cmpl	$-2147483638,%ebx
	jae	.L220
.L219:
	movl	$1,%eax
	jmp	.L221
.L220:
	movl	$0,%eax
.L221:
	cmpl	$-2147483637,%ebx
	jae	.L223
.L222:
	movl	$1,%ebx
	jmp	.L224
.L223:
	movl	$0,%ebx
.L224:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$-2147483638,%ebx
	movl	$0,-8(%ebp)
	cmpl	$1,%ebx
	jae	.L229
.L228:
	movl	$1,%edi
	jmp	.L230
.L229:
	movl	$0,%edi
.L230:
	cmpl	$19,%ebx
	jae	.L232
.L231:
	movl	$1,%esi
	jmp	.L233
.L232:
	movl	$0,%esi
.L233:
	cmpl	$20,%ebx
	jae	.L235
.L234:
	movl	$1,%edx
	jmp	.L236
.L235:
	movl	$0,%edx
.L236:
	cmpl	$-2147483638,%ebx
	jae	.L238
.L237:
	movl	$1,%ecx
	jmp	.L239
.L238:
	movl	$0,%ecx
.L239:
	cmpl	$-2147483637,%ebx
	jae	.L241
.L240:
	movl	$1,%eax
	jmp	.L242
.L241:
	movl	$0,%eax
.L242:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jae	.L244
.L243:
	movl	$1,%edi
	jmp	.L245
.L244:
	movl	$0,%edi
.L245:
	cmpl	$1,%ebx
	jae	.L247
.L246:
	movl	$1,%esi
	jmp	.L248
.L247:
	movl	$0,%esi
.L248:
	cmpl	$19,%ebx
	jae	.L250
.L249:
	movl	$1,%edx
	jmp	.L251
.L250:
	movl	$0,%edx
.L251:
	cmpl	$20,%ebx
	jae	.L253
.L252:
	movl	$1,%ecx
	jmp	.L254
.L253:
	movl	$0,%ecx
.L254:
	cmpl	$-2147483638,%ebx
	jae	.L256
.L255:
	movl	$1,%eax
	jmp	.L257
.L256:
	movl	$0,%eax
.L257:
	cmpl	$-2147483637,%ebx
	jae	.L259
.L258:
	movl	$1,%ebx
	jmp	.L260
.L259:
	movl	$0,%ebx
.L260:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$-2147483637,%ebx
	movl	$0,-4(%ebp)
	cmpl	$1,%ebx
	jae	.L265
.L264:
	movl	$1,%edi
	jmp	.L266
.L265:
	movl	$0,%edi
.L266:
	cmpl	$19,%ebx
	jae	.L268
.L267:
	movl	$1,%esi
	jmp	.L269
.L268:
	movl	$0,%esi
.L269:
	cmpl	$20,%ebx
	jae	.L271
.L270:
	movl	$1,%edx
	jmp	.L272
.L271:
	movl	$0,%edx
.L272:
	cmpl	$-2147483638,%ebx
	jae	.L274
.L273:
	movl	$1,%ecx
	jmp	.L275
.L274:
	movl	$0,%ecx
.L275:
	cmpl	$-2147483637,%ebx
	jae	.L277
.L276:
	movl	$1,%eax
	jmp	.L278
.L277:
	movl	$0,%eax
.L278:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jae	.L280
.L279:
	movl	$1,%edi
	jmp	.L281
.L280:
	movl	$0,%edi
.L281:
	cmpl	$1,%ebx
	jae	.L283
.L282:
	movl	$1,%esi
	jmp	.L284
.L283:
	movl	$0,%esi
.L284:
	cmpl	$19,%ebx
	jae	.L286
.L285:
	movl	$1,%edx
	jmp	.L287
.L286:
	movl	$0,%edx
.L287:
	cmpl	$20,%ebx
	jae	.L289
.L288:
	movl	$1,%ecx
	jmp	.L290
.L289:
	movl	$0,%ecx
.L290:
	cmpl	$-2147483638,%ebx
	jae	.L292
.L291:
	movl	$1,%eax
	jmp	.L293
.L292:
	movl	$0,%eax
.L293:
	cmpl	$-2147483637,%ebx
	jae	.L295
.L294:
	movl	$1,%ebx
	jmp	.L296
.L295:
	movl	$0,%ebx
.L296:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
.L297:
	popl	%edi
	popl	%esi
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
	movl	$0,%edx
	cmpl	$-2,%ebx
	jae	.L304
.L303:
	movl	$1,%ecx
	jmp	.L305
.L304:
	movl	$0,%ecx
.L305:
	cmpl	$-1,%ebx
	jae	.L307
.L306:
	movl	$1,%eax
	jmp	.L308
.L307:
	movl	$0,%eax
.L308:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$0,%ebx
	jae	.L310
.L309:
	movl	$1,%edx
	jmp	.L311
.L310:
	movl	$0,%edx
.L311:
	cmpl	$-2,%ebx
	jae	.L313
.L312:
	movl	$1,%ecx
	jmp	.L314
.L313:
	movl	$0,%ecx
.L314:
	cmpl	$-1,%ebx
	jae	.L316
.L315:
	movl	$1,%eax
	jmp	.L317
.L316:
	movl	$0,%eax
.L317:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$-2,%ebx
	movl	$0,%edx
	cmpl	$-2,%ebx
	jae	.L322
.L321:
	movl	$1,%ecx
	jmp	.L323
.L322:
	movl	$0,%ecx
.L323:
	cmpl	$-1,%ebx
	jae	.L325
.L324:
	movl	$1,%eax
	jmp	.L326
.L325:
	movl	$0,%eax
.L326:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$0,%ebx
	jae	.L328
.L327:
	movl	$1,%edx
	jmp	.L329
.L328:
	movl	$0,%edx
.L329:
	cmpl	$-2,%ebx
	jae	.L331
.L330:
	movl	$1,%ecx
	jmp	.L332
.L331:
	movl	$0,%ecx
.L332:
	cmpl	$-1,%ebx
	jae	.L334
.L333:
	movl	$1,%eax
	jmp	.L335
.L334:
	movl	$0,%eax
.L335:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$-1,%ebx
	movl	$0,%edx
	cmpl	$-2,%ebx
	jae	.L340
.L339:
	movl	$1,%ecx
	jmp	.L341
.L340:
	movl	$0,%ecx
.L341:
	cmpl	$-1,%ebx
	jae	.L343
.L342:
	movl	$1,%eax
	jmp	.L344
.L343:
	movl	$0,%eax
.L344:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$0,%ebx
	jae	.L346
.L345:
	movl	$1,%edx
	jmp	.L347
.L346:
	movl	$0,%edx
.L347:
	cmpl	$-2,%ebx
	jae	.L349
.L348:
	movl	$1,%ecx
	jmp	.L350
.L349:
	movl	$0,%ecx
.L350:
	cmpl	$-1,%ebx
	jae	.L352
.L351:
	movl	$1,%eax
	jmp	.L353
.L352:
	movl	$0,%eax
.L353:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L354:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,%ebx
	movl	$0,-24(%ebp)
	cmpl	$1,%ebx
	jbe	.L361
.L360:
	movl	$1,%edi
	jmp	.L362
.L361:
	movl	$0,%edi
.L362:
	cmpl	$19,%ebx
	jbe	.L364
.L363:
	movl	$1,%esi
	jmp	.L365
.L364:
	movl	$0,%esi
.L365:
	cmpl	$20,%ebx
	jbe	.L367
.L366:
	movl	$1,%edx
	jmp	.L368
.L367:
	movl	$0,%edx
.L368:
	cmpl	$-2147483638,%ebx
	jbe	.L370
.L369:
	movl	$1,%ecx
	jmp	.L371
.L370:
	movl	$0,%ecx
.L371:
	cmpl	$-2147483637,%ebx
	jbe	.L373
.L372:
	movl	$1,%eax
	jmp	.L374
.L373:
	movl	$0,%eax
.L374:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-24(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L376
.L375:
	movl	$1,%edi
	jmp	.L377
.L376:
	movl	$0,%edi
.L377:
	cmpl	$1,%ebx
	jbe	.L379
.L378:
	movl	$1,%esi
	jmp	.L380
.L379:
	movl	$0,%esi
.L380:
	cmpl	$19,%ebx
	jbe	.L382
.L381:
	movl	$1,%edx
	jmp	.L383
.L382:
	movl	$0,%edx
.L383:
	cmpl	$20,%ebx
	jbe	.L385
.L384:
	movl	$1,%ecx
	jmp	.L386
.L385:
	movl	$0,%ecx
.L386:
	cmpl	$-2147483638,%ebx
	jbe	.L388
.L387:
	movl	$1,%eax
	jmp	.L389
.L388:
	movl	$0,%eax
.L389:
	cmpl	$-2147483637,%ebx
	jbe	.L391
.L390:
	movl	$1,%ebx
	jmp	.L392
.L391:
	movl	$0,%ebx
.L392:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$1,%ebx
	movl	$1,-20(%ebp)
	cmpl	$1,%ebx
	jbe	.L397
.L396:
	movl	$1,%edi
	jmp	.L398
.L397:
	movl	$0,%edi
.L398:
	cmpl	$19,%ebx
	jbe	.L400
.L399:
	movl	$1,%esi
	jmp	.L401
.L400:
	movl	$0,%esi
.L401:
	cmpl	$20,%ebx
	jbe	.L403
.L402:
	movl	$1,%edx
	jmp	.L404
.L403:
	movl	$0,%edx
.L404:
	cmpl	$-2147483638,%ebx
	jbe	.L406
.L405:
	movl	$1,%ecx
	jmp	.L407
.L406:
	movl	$0,%ecx
.L407:
	cmpl	$-2147483637,%ebx
	jbe	.L409
.L408:
	movl	$1,%eax
	jmp	.L410
.L409:
	movl	$0,%eax
.L410:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-20(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L412
.L411:
	movl	$1,%edi
	jmp	.L413
.L412:
	movl	$0,%edi
.L413:
	cmpl	$1,%ebx
	jbe	.L415
.L414:
	movl	$1,%esi
	jmp	.L416
.L415:
	movl	$0,%esi
.L416:
	cmpl	$19,%ebx
	jbe	.L418
.L417:
	movl	$1,%edx
	jmp	.L419
.L418:
	movl	$0,%edx
.L419:
	cmpl	$20,%ebx
	jbe	.L421
.L420:
	movl	$1,%ecx
	jmp	.L422
.L421:
	movl	$0,%ecx
.L422:
	cmpl	$-2147483638,%ebx
	jbe	.L424
.L423:
	movl	$1,%eax
	jmp	.L425
.L424:
	movl	$0,%eax
.L425:
	cmpl	$-2147483637,%ebx
	jbe	.L427
.L426:
	movl	$1,%ebx
	jmp	.L428
.L427:
	movl	$0,%ebx
.L428:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$19,%ebx
	movl	$1,-16(%ebp)
	cmpl	$1,%ebx
	jbe	.L433
.L432:
	movl	$1,%edi
	jmp	.L434
.L433:
	movl	$0,%edi
.L434:
	cmpl	$19,%ebx
	jbe	.L436
.L435:
	movl	$1,%esi
	jmp	.L437
.L436:
	movl	$0,%esi
.L437:
	cmpl	$20,%ebx
	jbe	.L439
.L438:
	movl	$1,%edx
	jmp	.L440
.L439:
	movl	$0,%edx
.L440:
	cmpl	$-2147483638,%ebx
	jbe	.L442
.L441:
	movl	$1,%ecx
	jmp	.L443
.L442:
	movl	$0,%ecx
.L443:
	cmpl	$-2147483637,%ebx
	jbe	.L445
.L444:
	movl	$1,%eax
	jmp	.L446
.L445:
	movl	$0,%eax
.L446:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-16(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L448
.L447:
	movl	$1,%edi
	jmp	.L449
.L448:
	movl	$0,%edi
.L449:
	cmpl	$1,%ebx
	jbe	.L451
.L450:
	movl	$1,%esi
	jmp	.L452
.L451:
	movl	$0,%esi
.L452:
	cmpl	$19,%ebx
	jbe	.L454
.L453:
	movl	$1,%edx
	jmp	.L455
.L454:
	movl	$0,%edx
.L455:
	cmpl	$20,%ebx
	jbe	.L457
.L456:
	movl	$1,%ecx
	jmp	.L458
.L457:
	movl	$0,%ecx
.L458:
	cmpl	$-2147483638,%ebx
	jbe	.L460
.L459:
	movl	$1,%eax
	jmp	.L461
.L460:
	movl	$0,%eax
.L461:
	cmpl	$-2147483637,%ebx
	jbe	.L463
.L462:
	movl	$1,%ebx
	jmp	.L464
.L463:
	movl	$0,%ebx
.L464:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$20,%ebx
	movl	$1,-12(%ebp)
	cmpl	$1,%ebx
	jbe	.L469
.L468:
	movl	$1,%edi
	jmp	.L470
.L469:
	movl	$0,%edi
.L470:
	cmpl	$19,%ebx
	jbe	.L472
.L471:
	movl	$1,%esi
	jmp	.L473
.L472:
	movl	$0,%esi
.L473:
	cmpl	$20,%ebx
	jbe	.L475
.L474:
	movl	$1,%edx
	jmp	.L476
.L475:
	movl	$0,%edx
.L476:
	cmpl	$-2147483638,%ebx
	jbe	.L478
.L477:
	movl	$1,%ecx
	jmp	.L479
.L478:
	movl	$0,%ecx
.L479:
	cmpl	$-2147483637,%ebx
	jbe	.L481
.L480:
	movl	$1,%eax
	jmp	.L482
.L481:
	movl	$0,%eax
.L482:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-12(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L484
.L483:
	movl	$1,%edi
	jmp	.L485
.L484:
	movl	$0,%edi
.L485:
	cmpl	$1,%ebx
	jbe	.L487
.L486:
	movl	$1,%esi
	jmp	.L488
.L487:
	movl	$0,%esi
.L488:
	cmpl	$19,%ebx
	jbe	.L490
.L489:
	movl	$1,%edx
	jmp	.L491
.L490:
	movl	$0,%edx
.L491:
	cmpl	$20,%ebx
	jbe	.L493
.L492:
	movl	$1,%ecx
	jmp	.L494
.L493:
	movl	$0,%ecx
.L494:
	cmpl	$-2147483638,%ebx
	jbe	.L496
.L495:
	movl	$1,%eax
	jmp	.L497
.L496:
	movl	$0,%eax
.L497:
	cmpl	$-2147483637,%ebx
	jbe	.L499
.L498:
	movl	$1,%ebx
	jmp	.L500
.L499:
	movl	$0,%ebx
.L500:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$-2147483638,%ebx
	movl	$1,-8(%ebp)
	cmpl	$1,%ebx
	jbe	.L505
.L504:
	movl	$1,%edi
	jmp	.L506
.L505:
	movl	$0,%edi
.L506:
	cmpl	$19,%ebx
	jbe	.L508
.L507:
	movl	$1,%esi
	jmp	.L509
.L508:
	movl	$0,%esi
.L509:
	cmpl	$20,%ebx
	jbe	.L511
.L510:
	movl	$1,%edx
	jmp	.L512
.L511:
	movl	$0,%edx
.L512:
	cmpl	$-2147483638,%ebx
	jbe	.L514
.L513:
	movl	$1,%ecx
	jmp	.L515
.L514:
	movl	$0,%ecx
.L515:
	cmpl	$-2147483637,%ebx
	jbe	.L517
.L516:
	movl	$1,%eax
	jmp	.L518
.L517:
	movl	$0,%eax
.L518:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L520
.L519:
	movl	$1,%edi
	jmp	.L521
.L520:
	movl	$0,%edi
.L521:
	cmpl	$1,%ebx
	jbe	.L523
.L522:
	movl	$1,%esi
	jmp	.L524
.L523:
	movl	$0,%esi
.L524:
	cmpl	$19,%ebx
	jbe	.L526
.L525:
	movl	$1,%edx
	jmp	.L527
.L526:
	movl	$0,%edx
.L527:
	cmpl	$20,%ebx
	jbe	.L529
.L528:
	movl	$1,%ecx
	jmp	.L530
.L529:
	movl	$0,%ecx
.L530:
	cmpl	$-2147483638,%ebx
	jbe	.L532
.L531:
	movl	$1,%eax
	jmp	.L533
.L532:
	movl	$0,%eax
.L533:
	cmpl	$-2147483637,%ebx
	jbe	.L535
.L534:
	movl	$1,%ebx
	jmp	.L536
.L535:
	movl	$0,%ebx
.L536:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	movl	$-2147483637,%ebx
	movl	$1,-4(%ebp)
	cmpl	$1,%ebx
	jbe	.L541
.L540:
	movl	$1,%edi
	jmp	.L542
.L541:
	movl	$0,%edi
.L542:
	cmpl	$19,%ebx
	jbe	.L544
.L543:
	movl	$1,%esi
	jmp	.L545
.L544:
	movl	$0,%esi
.L545:
	cmpl	$20,%ebx
	jbe	.L547
.L546:
	movl	$1,%edx
	jmp	.L548
.L547:
	movl	$0,%edx
.L548:
	cmpl	$-2147483638,%ebx
	jbe	.L550
.L549:
	movl	$1,%ecx
	jmp	.L551
.L550:
	movl	$0,%ecx
.L551:
	cmpl	$-2147483637,%ebx
	jbe	.L553
.L552:
	movl	$1,%eax
	jmp	.L554
.L553:
	movl	$0,%eax
.L554:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L556
.L555:
	movl	$1,%edi
	jmp	.L557
.L556:
	movl	$0,%edi
.L557:
	cmpl	$1,%ebx
	jbe	.L559
.L558:
	movl	$1,%esi
	jmp	.L560
.L559:
	movl	$0,%esi
.L560:
	cmpl	$19,%ebx
	jbe	.L562
.L561:
	movl	$1,%edx
	jmp	.L563
.L562:
	movl	$0,%edx
.L563:
	cmpl	$20,%ebx
	jbe	.L565
.L564:
	movl	$1,%ecx
	jmp	.L566
.L565:
	movl	$0,%ecx
.L566:
	cmpl	$-2147483638,%ebx
	jbe	.L568
.L567:
	movl	$1,%eax
	jmp	.L569
.L568:
	movl	$0,%eax
.L569:
	cmpl	$-2147483637,%ebx
	jbe	.L571
.L570:
	movl	$1,%ebx
	jmp	.L572
.L571:
	movl	$0,%ebx
.L572:
	pushl	%ebx
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
.L573:
	popl	%edi
	popl	%esi
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
	movl	$0,%edx
	cmpl	$-2,%ebx
	jbe	.L580
.L579:
	movl	$1,%ecx
	jmp	.L581
.L580:
	movl	$0,%ecx
.L581:
	cmpl	$-1,%ebx
	jbe	.L583
.L582:
	movl	$1,%eax
	jmp	.L584
.L583:
	movl	$0,%eax
.L584:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L586
.L585:
	movl	$1,%edx
	jmp	.L587
.L586:
	movl	$0,%edx
.L587:
	cmpl	$-2,%ebx
	jbe	.L589
.L588:
	movl	$1,%ecx
	jmp	.L590
.L589:
	movl	$0,%ecx
.L590:
	cmpl	$-1,%ebx
	jbe	.L592
.L591:
	movl	$1,%eax
	jmp	.L593
.L592:
	movl	$0,%eax
.L593:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$-2,%ebx
	movl	$1,%edx
	cmpl	$-2,%ebx
	jbe	.L598
.L597:
	movl	$1,%ecx
	jmp	.L599
.L598:
	movl	$0,%ecx
.L599:
	cmpl	$-1,%ebx
	jbe	.L601
.L600:
	movl	$1,%eax
	jmp	.L602
.L601:
	movl	$0,%eax
.L602:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L604
.L603:
	movl	$1,%edx
	jmp	.L605
.L604:
	movl	$0,%edx
.L605:
	cmpl	$-2,%ebx
	jbe	.L607
.L606:
	movl	$1,%ecx
	jmp	.L608
.L607:
	movl	$0,%ecx
.L608:
	cmpl	$-1,%ebx
	jbe	.L610
.L609:
	movl	$1,%eax
	jmp	.L611
.L610:
	movl	$0,%eax
.L611:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$-1,%ebx
	movl	$1,%edx
	cmpl	$-2,%ebx
	jbe	.L616
.L615:
	movl	$1,%ecx
	jmp	.L617
.L616:
	movl	$0,%ecx
.L617:
	cmpl	$-1,%ebx
	jbe	.L619
.L618:
	movl	$1,%eax
	jmp	.L620
.L619:
	movl	$0,%eax
.L620:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$0,%ebx
	jbe	.L622
.L621:
	movl	$1,%edx
	jmp	.L623
.L622:
	movl	$0,%edx
.L623:
	cmpl	$-2,%ebx
	jbe	.L625
.L624:
	movl	$1,%ecx
	jmp	.L626
.L625:
	movl	$0,%ecx
.L626:
	cmpl	$-1,%ebx
	jbe	.L628
.L627:
	movl	$1,%eax
	jmp	.L629
.L628:
	movl	$0,%eax
.L629:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L630:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_a05
	pushl	_a04
	pushl	_a03
	pushl	_a02
	pushl	_a01
	pushl	_a00
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_a15
	pushl	_a14
	pushl	_a13
	pushl	_a12
	pushl	_a11
	pushl	_a10
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_a25
	pushl	_a24
	pushl	_a23
	pushl	_a22
	pushl	_a21
	pushl	_a20
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_a35
	pushl	_a34
	pushl	_a33
	pushl	_a32
	pushl	_a31
	pushl	_a30
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_a45
	pushl	_a44
	pushl	_a43
	pushl	_a42
	pushl	_a41
	pushl	_a40
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_a55
	pushl	_a54
	pushl	_a53
	pushl	_a52
	pushl	_a51
	pushl	_a50
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_b02
	pushl	_b01
	pushl	_b00
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	_b12
	pushl	_b11
	pushl	_b10
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	_b22
	pushl	_b21
	pushl	_b20
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
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

	.data
	.align	1
	.global	_s3
_s3:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
	.global	_s6
_s6:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	4
	.global	_a00
_a00:
	.long	0
	.align	4
	.global	_a01
_a01:
	.long	1
	.align	4
	.global	_a02
_a02:
	.long	1
	.align	4
	.global	_a03
_a03:
	.long	1
	.align	4
	.global	_a04
_a04:
	.long	1
	.align	4
	.global	_a05
_a05:
	.long	1
	.align	4
	.global	_a10
_a10:
	.long	0
	.align	4
	.global	_a11
_a11:
	.long	0
	.align	4
	.global	_a12
_a12:
	.long	1
	.align	4
	.global	_a13
_a13:
	.long	1
	.align	4
	.global	_a14
_a14:
	.long	1
	.align	4
	.global	_a15
_a15:
	.long	1
	.align	4
	.global	_a20
_a20:
	.long	0
	.align	4
	.global	_a21
_a21:
	.long	0
	.align	4
	.global	_a22
_a22:
	.long	0
	.align	4
	.global	_a23
_a23:
	.long	1
	.align	4
	.global	_a24
_a24:
	.long	1
	.align	4
	.global	_a25
_a25:
	.long	1
	.align	4
	.global	_a30
_a30:
	.long	0
	.align	4
	.global	_a31
_a31:
	.long	0
	.align	4
	.global	_a32
_a32:
	.long	0
	.align	4
	.global	_a33
_a33:
	.long	0
	.align	4
	.global	_a34
_a34:
	.long	1
	.align	4
	.global	_a35
_a35:
	.long	1
	.align	4
	.global	_a40
_a40:
	.long	0
	.align	4
	.global	_a41
_a41:
	.long	0
	.align	4
	.global	_a42
_a42:
	.long	0
	.align	4
	.global	_a43
_a43:
	.long	0
	.align	4
	.global	_a44
_a44:
	.long	0
	.align	4
	.global	_a45
_a45:
	.long	1
	.align	4
	.global	_a50
_a50:
	.long	0
	.align	4
	.global	_a51
_a51:
	.long	0
	.align	4
	.global	_a52
_a52:
	.long	0
	.align	4
	.global	_a53
_a53:
	.long	0
	.align	4
	.global	_a54
_a54:
	.long	0
	.align	4
	.global	_a55
_a55:
	.long	0
	.align	4
	.global	_b00
_b00:
	.long	0
	.align	4
	.global	_b01
_b01:
	.long	1
	.align	4
	.global	_b02
_b02:
	.long	1
	.align	4
	.global	_b10
_b10:
	.long	0
	.align	4
	.global	_b11
_b11:
	.long	0
	.align	4
	.global	_b12
_b12:
	.long	1
	.align	4
	.global	_b20
_b20:
	.long	0
	.align	4
	.global	_b21
_b21:
	.long	0
	.align	4
	.global	_b22
_b22:
	.long	0
