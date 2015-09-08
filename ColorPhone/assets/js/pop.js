(function(){
	var Tip = function(){
		var _this = this;
		var pop = createPop();
		this.show = function(){
			document.body.appendChild(pop);
			setTimeout(function(){
				pop.remove();
			}, 1000);
		};
		this.hide = function(){
			pop.remove();
		};
		
		function createPop(){
			var html = '<div style="position:fixed; top:80px; text-align:center; width:100%;"><img src="img/pop.png" width="65" height="30" /></div>';
			var div = document.createElement("div");
			div.innerHTML = html;
			var float = div.children[0];
			return float;
		}
	};
	
	window.Tip = Tip;
})();