class Flow {
  /**
   * 获取当前登录用户信息
   *
   * @returns 登录用户昵称、头像信息，包括角色和权限
   */
  static createNew(pager, showNodeForm, nodeForm, showLineForm, lineForm) {
    // pager事件定义
    var clickEvent = (t) => new CustomEvent('click', {
      detail: { t }
    })
    var removerectEvent = (t) => new CustomEvent('removerect', {
      detail: { t }
    })
    var removepathEvent = (t) => new CustomEvent('removepath', {
      detail: { t }
    })
    var rectresizeEvent = (t) => new CustomEvent('rectresize', {
      detail: { t }
    })
    //主对象
    var flow = {};
    flow.rectarr = {}; //节点集合
    flow.patharr = {}; //连线集合
    flow.begin; //连线起点(添加连线时用到)
    flow.tmp;  //临时点(为确定连线起点和终点)
    flow.end;  //连线终点(添加连线时用到)
    flow.forms;
    flow.positions;
    flow.users;
    flow.currNode;
    flow.config = {
        editable: true,
        lineHeight: 15,
        basePath: "",
        rect: {
            attr: {
                x: 300,
                y: 100,
                width: 100,
                height: 50,
                r: 5,
                fill: "90-#fff-#C0C0C0",
                stroke: "#000",
                "stroke-width": 3
            },
            data: { //节点属性数据
                id: '',
                name: '新建节点',
                rectType: '0',
                formId: '',
                cooperation: '0',
                virtual: 0,
                beentrusted: 1,
                departmentId: '',
                positionId: '',
                userId: '',
                userName: '',
                remark: '',
                isApproval: '0', // 是否审批节点，1-是，0-否
                dealed: false // 已处理过
            },
            text: {
                text: "新建节点",
                "font-size": 12
            },
            margin: 5
        },
        path: {
            attr: {
                path: {
                    path: "M10 10L100 100",
                    stroke: "#808080",
                    fill: "none",
                    "stroke-width": 4
                },
                arrow: {
                    path: "M10 10L10 10",
                    stroke: "#808080",
                    fill: "#808080",
                    "stroke-width": 4,
                    radius: 4
                },
                fromDot: {
                    width: 5,
                    height: 5,
                    stroke: "#fff",
                    fill: "#000",
                    cursor: "move",
                    "stroke-width": 2
                },
                toDot: {
                    width: 5,
                    height: 5,
                    stroke: "#fff",
                    fill: "#000",
                    cursor: "move",
                    "stroke-width": 2
                },
                bigDot: {
                    width: 5,
                    height: 5,
                    stroke: "#fff",
                    fill: "#000",
                    cursor: "move",
                    "stroke-width": 2
                },
                smallDot: {
                    width: 5,
                    height: 5,
                    stroke: "#fff",
                    fill: "#000",
                    cursor: "move",
                    "stroke-width": 3
                }
            },
            data: { //连线属性数据
                id: '',
                name: '',
                formId: '',
                field: '', //条件字段
                operator: '', //条件符号，如‘=’
                operatorValue: '', //条件值
                remark: '',
                dealed: false // 已处理过
            },
            text: {
                text: "",
                cursor: "move",
                background: "#000",
                "font-size": 12
            },
            textPos: {
                x: 0,
                y: -10
            }
        },
        restore: ""
    };
    flow.util = { //方法集
        isLine: function (g, f, e) {
            var d, c;
            if ((g.x - e.x) == 0) {
                d = 1;
            } else {
                d = (g.y - e.y) / (g.x - e.x);
            }
            c = (f.x - e.x) * d + e.y;
            if ((f.y - c) < 10 && (f.y - c) > -10) {
                f.y = c;
                return true;
            }
            return false;
        },
        center: function (d, c) {
            return {
                x: (d.x - c.x) / 2 + c.x,
                y: (d.y - c.y) / 2 + c.y
            }
        },
        nextId: (function () {
            var c = 0;
            return function () {
                return ++c;
            }
        })(),
        nextPathId: (function () {
            var c = 0;
            return function () {
                return ++c;
            }
        })(),
        connPoint: function (j, d) {
            var c = d,
            e = {
                x: j.x + j.width / 2,
                y: j.y + j.height / 2
            };
            var l = (e.y - c.y) / (e.x - c.x);
            l = isNaN(l) ? 0 : l;
            var k = j.height / j.width;
            var h = c.y < e.y ? -1 : 1,
            f = c.x < e.x ? -1 : 1,
            g,
            i;
            if (Math.abs(l) > k && h == -1) {
                g = e.y - j.height / 2;
                i = e.x + h * j.height / 2 / l;
            } else {
                if (Math.abs(l) > k && h == 1) {
                    g = e.y + j.height / 2;
                    i = e.x + h * j.height / 2 / l;
                } else {
                    if (Math.abs(l) < k && f == -1) {
                        g = e.y + f * j.width / 2 * l;
                        i = e.x - j.width / 2;
                    } else {
                        if (Math.abs(l) < k && f == 1) {
                            g = e.y + j.width / 2 * l;
                            i = e.x + j.width / 2;
                        }
                    }
                }
            }
            return {
                x: i,
                y: g
            }
        },
        arrow: function (l, k, d) {
            var g = Math.atan2(l.y - k.y, k.x - l.x) * (180 / Math.PI);
            var h = k.x - d * Math.cos(g * (Math.PI / 180));
            var f = k.y + d * Math.sin(g * (Math.PI / 180));
            var e = h + d * Math.cos((g + 120) * (Math.PI / 180));
            var j = f - d * Math.sin((g + 120) * (Math.PI / 180));
            var c = h + d * Math.cos((g + 240) * (Math.PI / 180));
            var i = f - d * Math.sin((g + 240) * (Math.PI / 180));
            return [k, {
                x: e,
                y: j
            },
            {
                x: c,
                y: i
            }]
        },
        attr: function (ele, d) { //远程节点数据赋值到rect对象
            if (ele && d) {
                for (var p in d) {
                    ele[p] = d[p];
                }
            }
        },
        addpath: function (c) { //添加连线
            if (flow.begin && flow.end) {
                if (!flow.util.checkpath(flow.begin, flow.end)) {
                    var p = new flow.path(flow.begin, flow.end, c);
                    flow.patharr[p.getId()] = p;

                    //加载元素属性
                    //flow.util.getPathPropertie(p);

                    p.select();
                }
            }
        },
        checkpath: function (begin, end) { //检查连线是否存在
            for (var p in flow.patharr) {
                if (flow.patharr[p]) {
                    if ((flow.patharr[p].from().getId() == begin.getId() && flow.patharr[p].to().getId() == end.getId())
                        || (flow.patharr[p].from().getId() == end.getId() && flow.patharr[p].to().getId() == begin.getId())) {
                        return true;
                    }
                }
            }
            return false;
        },
        addrect: function () { //添加节点
            var p = new flow.rect();
            flow.rectarr[p.getId()] = p;

            //加载元素属性
            //flow.util.getRectPropertie(p);

            p.select();
        },
        getRectPropertie: function (p) {
            nodeForm.id = p.getId();
            nodeForm.name = p.attr('name');
            nodeForm.rectType = p.attr('rectType');
            nodeForm.isApproval = p.attr('isApproval');
            nodeForm.formId = p.attr('formId');
            nodeForm.cooperation = p.attr('cooperation');
            nodeForm.departmentId = p.attr('departmentId');
            nodeForm.positionId = p.attr('positionId');
            nodeForm.userId = p.attr('userId');
        },
        getPathPropertie: function (p) {
            lineForm.id = p.getId();
            lineForm.name = p.attr({ path: 'name' });
            lineForm.operator = p.attr({ path: 'operator' });
            lineForm.formId = p.attr({ path: 'formId' });
            lineForm.field = p.attr({ path: 'field' });
            lineForm.operatorValue = p.attr({ path: 'operatorValue' });
        },
        check: function () { //流程检查
            if (Object.keys(flow.patharr).length === 0 || Object.keys(flow.rectarr).length === 0) return false;
            return true;
        },
        saveFlow: function() { //保存流程
            var nodes = "";
            for (var rect in flow.rectarr) {
                nodes += flow.rectarr[rect].toJson() + ";";
            }
            nodes = nodes.substring(0, nodes.length - 1);
            var links = "";
            for (var path in flow.patharr) {
                links += flow.patharr[path].toJson() + ";";
            }
            links = links.substring(0, links.length - 1);
            var result = {};
            result.nodes = nodes;
            result.links = links;
            return result;
        },
        groupSeq: function (r) { //得到节点的序号
            var beginNum = 0; //起点连线数量
            var endNum = 0; //终点连线数量
            for (var path in flow.patharr) {
                if (flow.patharr[path] && flow.patharr[path].from().getId() == r.getId()) {
                    beginNum++;
                }
                if (flow.patharr[path] && flow.patharr[path].to().getId() == r.getId()) {
                    endNum++;
                }
            }
            if (beginNum > 0 && endNum == 0) { //起点
                return 1;
            }
            else if (beginNum == 0 && endNum > 0) { //终点
                return 9;
            }
            else {
                return 2;
            }
        },
        handleNodeName: function(id, value){
            flow.rectarr[id].setattr('name', value);
            flow.rectarr[id].settext(value);
        },
        handleRectType: function(id, value){
            flow.rectarr[id].setattr('rectType', value);
        },
        handleIsApproval: function(id, value){
            flow.rectarr[id].setattr('isApproval', value);
            if (value == '1'){
                nodeForm.showFormId = false;
            }
            else{
                nodeForm.showFormId = true;
            }
        },
        handleFormId: function(id, value){
            flow.rectarr[id].setattr('formId', value);
        },
        handleCooperation: function(id, value){
            flow.rectarr[id].setattr('cooperation', value);
        },
        handleDepartment: function(id, value){
            flow.rectarr[id].setattr('departmentId', value);
        },
        handlePosition: function(id, value){
            flow.rectarr[id].setattr('positionId', value);
        },
        handleUser: function(id, value){
            flow.rectarr[id].setattr('userId', value);
        },
        handleLineName: function(id, value){
            flow.patharr[id].setattr('name', value);
            flow.patharr[id].settext(value);
        },
        handleOperator: function(id, value){
            flow.patharr[id].setattr('operator', value);
        },
        handleLineFormId: function(id, value){
            flow.patharr[id].setattr('formId', value);
        },
        handleLineField: function(id, value){
            flow.patharr[id].setattr('field', value);
        },
        handleOperatorValue: function(id, value){
            flow.patharr[id].setattr('operatorValue', value);
        },
        /**
         * 深度合并代码
         */
        deepMerge: function(target, source){
            if (source.constructor === Object) {
                for (let key in source) {
                  if (source[key] && source[key].constructor === Object) {
                    // 递归合并对象中的对象
                    if (target[key] && target[key].constructor === Object) {
                      this.deepMerge(target[key], source[key]);
                    } else {
                      target[key] = this.deepMerge({}, source[key]);
                    }
                  } else {
                    // 非对象值直接赋值
                    target[key] = source[key];
                  }
                }
            }
            return target;
        }
    };
    flow.rect = function (rect) {
        var u = this;
        var nextId = flow.util.nextId();
        var g = "rect" + nextId;
        var a = {};
        if (rect) {
            // a = { ...flow.config.rect, ...rect };
            a = JSON.parse(JSON.stringify(flow.config.rect));
            flow.util.deepMerge(a, rect);
        }
        else {
            a = flow.config.rect;
            a.attr.y = 100 + (nextId - 1) * 120;
        }
        var t = pager.rect(a.attr.x, a.attr.y, a.attr.width, a.attr.height, a.attr.r).attr(a.attr);
        flow.util.attr(t, a.data); //节点属性
        if (a.data.dealed) t.attr("fill", "90-#fff-#0b92d5");
        var f = pager.text(a.attr.x + a.attr.width / 2, a.attr.y + a.attr.height / 2, a.text.text).attr(a.text);
        var n = pager.text(a.attr.x + 120, a.attr.y + 8, '').attr("fill", "rgb(20, 165, 236)");
        var q = {
            x: a.attr.x - a.margin,
            y: a.attr.y - a.margin,
            width: a.attr.width + a.margin * 2,
            height: a.attr.height + a.margin * 2
        };
        var x, v;
        t.drag(function (r, o) {
            A(r, o)
        },
        function () {
            z()
        },
        function () {
            l()
        });
        f.drag(function (r, o) {
            A(r, o)
        },
        function () {
            z()
        },
        function () {
            l()
        });
        n.drag(function (r, o) {
            A(r, o)
        },
        function () {
            z()
        },
        function () {
            l()
        });
        var A = function (dx, dy) {
            var o = (x + dx);
            var G = (v + dy);
            q.x = o - a.margin;
            q.y = G - a.margin;
            B();
        };
        var z = function () {
            x = t.attr("x");
            v = t.attr("y");
            t.attr({
                opacity: 0.5
            });
            f.attr({
                opacity: 0.5
            });
        };
        var l = function () {
            t.attr({
                opacity: 1
            });
            f.attr({
                opacity: 1
            });
        };

        t.click(function(){ 
          handleClick();
        })

        f.click(function(){ 
          handleClick();
        })

        var handleClick = function () {
          showNodeForm.value = true;
          showLineForm.value = false;
          if (flow.currNode != u) {
              t.attr("fill", "90-#fff-#0b92d5");
              if (flow.begin) {
                  if (flow.begin != u) {
                      if (flow.end) {
                          if (flow.end != u) {
                              n.show();
                              n.attr("text", "[后继]");
                              flow.tmp = flow.end;
                              flow.end = u;
                          }
                      }
                      else {
                          n.show();
                          n.attr("text", "[后继]");
                          flow.end = u;
                      }
                  }
                  else {
                      if (flow.end) {
                          n.show();
                          n.attr("text", "[后继]");
                          flow.tmp = flow.end;
                          flow.end = u;
                      }
                  }
              }
              else {
                  n.show();
                  n.attr("text", "[前置]");
                  flow.begin = u;
              }
              pager.canvas.dispatchEvent(clickEvent(u));
              flow.currNode = u;
              flow.util.getRectPropertie(flow.rectarr[g]);
          }
        }

        var j = function (r) {
            if (!rectresizeEvent) return;
            if (r && r.getId() != g) {
                t.attr("fill", "90-#fff-#C0C0C0");
                if (r.getId().substring(0, 4) == "rect") {
                    if (flow.begin == u) {
                        //终点非当前选中节点
                        if (flow.tmp && flow.tmp.getId() != r.getId()) {
                            n.hide();
                            n.attr("text", '');
                        }
                    }
                    else if (flow.tmp == u) { //终点改为起点
                        n.show();
                        n.attr("text", "[前置]");
                        flow.begin = u;
                    }
                    else {
                        n.hide();
                        n.attr("text", '');
                    }
                }
            }
        };

        pager.canvas.addEventListener('click', ({detail}) => {
          j(detail.t);
        })

        function B() {
            var F = q.x + a.margin,
            r = q.y + a.margin,
            G = q.width - a.margin * 2,
            o = q.height - a.margin * 2;
            t.attr({
                x: F,
                y: r,
                width: G,
                height: o
            });
            f.attr({
                x: F + G / 2,
                y: r + o / 2
            });
            n.attr({
                x: F + 120,
                y: r + 8
            });
            pager.canvas.dispatchEvent(rectresizeEvent(u));
        }
        this.toJson = function () {
            var seq = flow.util.groupSeq(u);
            var r = g + "," + Math.round(t.attr("x")) + "," + Math.round(t.attr("y")) + "," + t["id"] + "," + t["name"]
            + "," + t["rectType"] + "," + t["formId"] + "," + t["virtual"] + "," + t["cooperation"]
            + "," + t["departmentId"] + "," + t["positionId"] + "," + t["userId"] + "," + t["remark"] + "," + seq + "," + t["isApproval"];
            return r;
        };
        this.getBBox = function () {
            return q;
        };
        this.getId = function () {
            return g;
        };
        this.text = function () {
            return f.attr("text");
        };
        this.settext = function (text) {
            f.attr("text", text);
        };
        this.attr = function (o) {
            if (o) {
                return t[o];
            }
        };
        this.setattr = function (o, v) {
            if (o) {
                t[o] = v;
            }
        };
        this.remove = function () {
            t.remove();
            f.remove();
            n.remove();
        };
        this.select = function () {
          handleClick();
        };
    };
    flow.path = function (begin, end, path) {
        var v = this;
        var i,
        t,
        f,
        y,
        w,
        x;
        var a = {};
        if (path) {
            // a = {...flow.config.path, ...path};
            a = JSON.parse(JSON.stringify(flow.config.path));
            flow.util.deepMerge(a, path);
        }
        else {
            a = flow.config.path;
        }
        var h = a.textPos;
        var g = "path" + flow.util.nextPathId();
        //绘制连线上的点
        function p(G, H, D, L) {
            var F = this,
            M = G,
            r, o = D,
            O = L,
            K, I, N = H;
            switch (M) {
                case "from":
                    r = pager.rect(H.x - a.attr.fromDot.width / 2, H.y - a.attr.fromDot.height / 2, a.attr.fromDot.width, a.attr.fromDot.height).attr(a.attr.fromDot);
                    break;
                case "big":
                    r = pager.rect(H.x - a.attr.bigDot.width / 2, H.y - a.attr.bigDot.height / 2, a.attr.bigDot.width, a.attr.bigDot.height).attr(a.attr.bigDot);
                    break;
                case "small":
                    r = pager.rect(H.x - a.attr.smallDot.width / 2, H.y - a.attr.smallDot.height / 2, a.attr.smallDot.width, a.attr.smallDot.height).attr(a.attr.smallDot);
                    break;
                case "to":
                    r = pager.rect(H.x - a.attr.toDot.width / 2, H.y - a.attr.toDot.height / 2, a.attr.toDot.width, a.attr.toDot.height).attr(a.attr.toDot);
                    break
            }
            if (r && (M == "big" || M == "small")) {
                r.drag(function (Q, P) { //拖动处理函数
                    C(Q, P)
                },
                function () { //拖动开始的处理函数
                    J()
                },
                function () { //拖动结束的处理函数
                    E()
                });
                var C = function (R, Q) {
                    var P = (K + R), S = (I + Q);
                    F.moveTo(P, S)
                };
                var J = function () {
                    if (M == "big") {
                        K = r.attr("x") + a.attr.bigDot.width / 2;
                        I = r.attr("y") + a.attr.bigDot.height / 2
                    }
                    if (M == "small") {
                        K = r.attr("x") + a.attr.smallDot.width / 2;
                        I = r.attr("y") + a.attr.smallDot.height / 2
                    }
                };
                var E = function () { }
            }
            this.type = function (P) {
                if (P) {
                    M = P
                } else {
                    return M
                }
            };
            this.node = function (P) {
                if (P) {
                    r = P
                } else {
                    return r
                }
            };
            this.left = function (P) {
                if (P) {
                    o = P
                } else {
                    return o
                }
            };
            this.right = function (P) {
                if (P) {
                    O = P
                } else {
                    return O
                }
            };
            this.remove = function () {
                o = null;
                O = null;
                r.remove()
            };
            this.pos = function (P) {
                if (P) {
                    N = P;
                    r.attr({
                        x: N.x - r.attr("width") / 2,
                        y: N.y - r.attr("height") / 2
                    });
                    return this
                } else {
                    return N
                }
            };
            this.moveTo = function (Q, T) {
                this.pos({
                    x: Q,
                    y: T
                });
                switch (M) {
                    case "from":
                        if (O && O.right() && O.right().type() == "to") {
                            O.right().pos(flow.util.connPoint(end.getBBox(), N))
                        }
                        if (O && O.right()) {
                            O.pos(flow.util.center(N, O.right().pos()))
                        }
                        break;
                    case "big":
                        if (O && O.right() && O.right().type() == "to") {
                            O.right().pos(flow.util.connPoint(end.getBBox(), N))
                        }
                        if (o && o.left() && o.left().type() == "from") {
                            o.left().pos(flow.util.connPoint(begin.getBBox(), N))
                        }
                        if (O && O.right()) {
                            O.pos(flow.util.center(N, O.right().pos()))
                        }
                        if (o && o.left()) {
                            o.pos(flow.util.center(N, o.left().pos()))
                        }
                        var S = {
                            x: N.x,
                            y: N.y
                        };
                        if (flow.util.isLine(o.left().pos(), S, O.right().pos())) {
                            M = "small";
                            r.attr(a.attr.smallDot);
                            this.pos(S);
                            var P = o;
                            o.left().right(o.right());
                            o = o.left();
                            P.remove();
                            var R = O;
                            O.right().left(O.left());
                            O = O.right();
                            R.remove()
                        }
                        break;
                    case "small":
                        if (o && O && !flow.util.isLine(o.pos(), {
                            x: N.x,
                            y: N.y
                        }, O.pos())) {
                            M = "big";
                            r.attr(a.attr.bigDot);
                            var P = new p("small", flow.util.center(o.pos(), N), o, o.right());
                            o.right(P);
                            o = P;
                            var R = new p("small", flow.util.center(O.pos(), N), O.left(), O);
                            O.left(R);
                            O = R
                        }
                        break;
                    case "to":
                        if (o && o.left() && o.left().type() == "from") {
                            o.left().pos(flow.util.connPoint(begin.getBBox(), N))
                        }
                        if (o && o.left()) {
                            o.pos(flow.util.center(N, o.left().pos()))
                        }
                        break
                }
                m()
            }
        }
        function j() {
            var D, C, E = begin.getBBox(), //起点属性
            F = end.getBBox(), //终点属性
            r,
            o;
            r = flow.util.connPoint(E, {
                x: F.x + F.width / 2,
                y: F.y + F.height / 2
            });
            o = flow.util.connPoint(F, r);
            D = new p("from", r, null, new p("small", {
                x: (r.x + o.x) / 2,
                y: (r.y + o.y) / 2
            }));
            D.right().left(D);
            C = new p("to", o, D.right(), null);
            D.right().right(C);
            this.toPathString = function () {
                if (!D) {
                    return ""
                }
                var J = D,
                I = "M" + J.pos().x + " " + J.pos().y,
                H = "";
                while (J.right()) {
                    J = J.right();
                    I += "L" + J.pos().x + " " + J.pos().y
                }
                var G = flow.util.arrow(J.left().pos(), J.pos(), a.attr.arrow.radius);
                H = "M" + G[0].x + " " + G[0].y + "L" + G[1].x + " " + G[1].y + "L" + G[2].x + " " + G[2].y + "z";
                return [I, H]
            };
            this.toJson = function () {
                var G = "[", H = D;
                while (H) {
                    if (H.type() == "big") {
                        G += "{x:" + Math.round(H.pos().x) + ",y:" + Math.round(H.pos().y) + "},"
                    }
                    H = H.right()
                }
                if (G.substring(G.length - 1, G.length) == ",") {
                    G = G.substring(0, G.length - 1)
                }
                G += "]";
                return G
            };
            this.restore = function (H) {
                var I = H, J = D.right();
                for (var G = 0; G < I.length; G++) {
                    J.moveTo(I[G].x, I[G].y);
                    J.moveTo(I[G].x, I[G].y);
                    J = J.right()
                }
                this.hide()
            };
            this.fromDot = function () {
                return D
            };
            this.toDot = function () {
                return C
            };
            this.midDot = function () {
                var H = D.right(), G = D.right().right();
                while (G.right() && G.right().right()) {
                    G = G.right().right();
                    H = H.right()
                }
                return H
            };
            this.show = function () {
                var G = D;
                while (G) {
                    G.node().show();
                    G = G.right()
                }
            };
            this.hide = function () {
                var G = D;
                while (G) {
                    G.node().hide();
                    G = G.right()
                }
            };
            this.remove = function () {
                var G = D;
                while (G) {
                    if (G.right()) {
                        G = G.right();
                        G.left().remove()
                    } else {
                        G.remove();
                        G = null
                    }
                }
            }
        }
        i = pager.path(a.attr.path.path).attr(a.attr.path);
        flow.util.attr(i, a.data);
        t = pager.path(a.attr.arrow.path).attr(a.attr.arrow);
        if (a.data.dealed) {
            i.attr("stroke", "#0b92d5");
            t.attr("stroke", "#0b92d5");
        }
        x = new j();
        x.hide();
        f = pager.text(0, 0, a.text.text).attr(a.text);
        f.drag(function (r, o) {
            if (!flow.config.editable) {
                return
            }
            f.attr({
                x: y + r,
                y: w + o
            })
        },
        function () {
            y = f.attr("x");
            w = f.attr("y")
        },
        function () {
            var o = x.midDot().pos();
            h = {
                x: f.attr("x") - o.x,
                y: f.attr("y") - o.y
            }
        });
        m();
        //连线点击事件
        i.click(function(){ 
          handleClick();
        })

        t.click(function(){ 
          handleClick();
        })

        f.click(function(){ 
          handleClick();
        })
        
        var handleClick = function(){
          showNodeForm.value = false;
          showLineForm.value = true;
          pager.canvas.dispatchEvent(clickEvent(v));
          flow.currNode = v;

          flow.util.getPathPropertie(flow.patharr[g]);
          return false;
        }
        //pager点击事件
        var l = function (C) {
            if (!C) return;
            if (C && C.getId() == g) {
                x.show();
            } else {
                x.hide()
            }
        };
        pager.canvas.addEventListener('click', ({detail}) => {
            l(detail.t);
        })
        //删除节点事件（每条连线都会触发）
        var A = function (r) {
            if (!flow.config.editable) {
                return
            }
            if (r && (r.getId() == begin.getId() || r.getId() == end.getId())) {
                pager.canvas.dispatchEvent(removepathEvent(v));
            }
        };
        pager.canvas.addEventListener('removerect', ({detail}) => {
            A(detail.t);
        })
        var d = function (D) {
            if (begin && begin.getId() == D.getId()) {
                var o;
                if (x.fromDot().right().right().type() == "to") {
                    o = {
                        x: end.getBBox().x + end.getBBox().width / 2,
                        y: end.getBBox().y + end.getBBox().height / 2
                    }
                } else {
                    o = x.fromDot().right().right().pos()
                }
                var r = flow.util.connPoint(begin.getBBox(), o);
                x.fromDot().moveTo(r.x, r.y);
                m();
            }
            if (end && end.getId() == D.getId()) {
                var o;
                if (x.toDot().left().left().type() == "from") {
                    o = {
                        x: begin.getBBox().x + begin.getBBox().width / 2,
                        y: begin.getBBox().y + begin.getBBox().height / 2
                    }
                } else {
                    o = x.toDot().left().left().pos()
                }
                var r = flow.util.connPoint(end.getBBox(), o);
                x.toDot().moveTo(r.x, r.y);
                m();
            }
        };
        pager.canvas.addEventListener('rectresize', ({detail}) => {
            d(detail.t);
        })
        this.from = function () {
            return begin
        };
        this.to = function () {
            return end
        };
        this.toJson = function () {
            //                    var r = "{From:'" + begin.getId() + "',To:'" + end.getId() + "',LinkName:'" + f.attr("text") + "',X:" + Math.round(h.x) + ",Y:" + Math.round(h.y) + ",Operator:'"
            //                    + i["operatortext"] + "',OperatorValue:'" + i["condition"] + "',Description:'" + i["remark"] + "'";
            //                    r += "}";
            var r = begin.getId() + "," + end.getId() + "," + i["name"] + "," + Math.round(h.x) + "," + Math.round(h.y) + ","
                + i["formId"] + "," + i["field"] + "," + i["operator"] + "," + i["operatorValue"] + "," + i["remark"];
            return r;
        };
        this.restore = function (o) {
            var r = o;
            a = $.extend(true, a, o);
            x.restore(r.dots)
        };
        this.remove = function () {
            x.remove();
            i.remove();
            t.remove();
            f.remove();
            try {
                pager.canvas.removeEventListener('click', ({detail}) => {
                    l(detail.t);
                })
            } catch (o) { }
            try {
                pager.canvas.removeEventListener('removerect', ({detail}) => {
                    A(detail.t);
                })
            } catch (o) { }
            try {
                pager.canvas.removeEventListener('rectresize', ({detail}) => {
                    d(detail.t);
                })
            } catch (o) { }
        };
        function m() {
            var r = x.toPathString(), o = x.midDot().pos();
            i.attr({
                path: r[0]
            });
            t.attr({
                path: r[1]
            });
            f.attr({
                x: o.x + h.x,
                y: o.y + h.y
            })
        }
        this.getId = function () {
            return g
        };
        this.text = function () {
            return f.attr("text")
        };
        this.attr = function (o) {
            if (o && o.path) {
                return i[o.path]
            }
            if (o && o.arrow) {
                return t[o.arrow]
            }
        }
        this.settext = function (text) {
            f.attr("text", text);
        };
        this.setattr = function (o, v) {
            if (o) {
                i[o] = v;
            }
        };
        this.select = function () {
            handleClick();
        };
    };
    flow.init = function (d, steps) {
        //Delete按键删除事件
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Delete') {
              // 按下了Delete键
              var j = flow.currNode;
                if (j) {
                    if (j.getId().substring(0, 4) == "rect") {
                        pager.canvas.dispatchEvent(removerectEvent(j));
                    } else {
                        if (j.getId().substring(0, 4) == "path") {
                            pager.canvas.dispatchEvent(removerectEvent(j));
                        }
                    }
                    flow.currNode = undefined;
                }
            }
          });
        //删除事件
        var w = function (i) {
            if (i.getId().substring(0, 4) == "rect") {
                delete flow.rectarr[i.getId()];
                i.remove();
            } else {
                if (i.getId().substring(0, 4) == "path") {
                    delete flow.patharr[i.getId()];
                    i.remove();
                }
            }
        };
        pager.canvas.addEventListener('removepath', ({detail}) => {
            w(detail.t);
        })
        pager.canvas.addEventListener('removerect', ({detail}) => {
            w(detail.t);
        })
        //初始化
        var z = {};
        if (d) {
            if (d.data.rects) {
                for (var s in d.data.rects) {
                    if (steps && steps.includes(d.data.rects[s].data.id)) d.data.rects[s].data.dealed = true;
                    var r = new flow.rect(d.data.rects[s]);
                    z[d.data.rects[s].data.id] = r;
                    flow.rectarr[r.getId()] = r;
                }
            }
            if (d.data.paths) {
                for (var s in d.data.paths) {
                    if (steps && steps.includes(d.data.paths[s].from.toString()) && steps.includes(d.data.paths[s].to.toString())) d.data.paths[s].data.dealed = true;
                    var n = new flow.path(z[d.data.paths[s].from], z[d.data.paths[s].to], d.data.paths[s]);
                    flow.patharr[n.getId()] = n;
                }
            }
        }
    };
            
    return flow;
  }
}

export default Flow;