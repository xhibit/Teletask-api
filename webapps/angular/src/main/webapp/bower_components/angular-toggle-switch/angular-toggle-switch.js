angular.module('toggle-switch', ['ng']).directive('toggleSwitch', function () {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            model: '=',
            onValue: '@',
            offValue: '@',
            disabled: '@',
            onLabel: '@',
            offLabel: '@',
            knobLabel: '@',
            onChange: '&'
        },
        template: '<div class="switch" ng-class="{ \'disabled\': disabled }"><div class="switch-animate" ng-class="{\'switch-off\': (model == offValue), \'switch-on\': (model == onValue)}"><span class="switch-left" ng-bind="onLabel"></span><span class="knob" ng-bind="knobLabel"></span><span class="switch-right" ng-bind="offLabel"></span></div></div>',
        link: function (scope, element, attrs) {
            if (!attrs.onLabel) {
                attrs.onLabel = 'On';
            }
            if (!attrs.offLabel) {
                attrs.offLabel = 'Off';
            }
            if (!attrs.onValue) {
                attrs.onValue = 'true';
            }
            if (!attrs.offValue) {
                attrs.offValue = 'false';
            }
            if (!attrs.knobLabel) {
                attrs.knobLabel = '\u00a0';
            }
            if (!attrs.disabled) {
                attrs.disabled = false;
            }
            if (!attrs.onChange) {
                attrs.onChange = function(newValue) { scope.model = newValue; };
            }

            element.on('click', function () {
                scope.$apply(scope.toggle);
            });

            scope.toggle = function toggle() {
                if (!scope.disabled) {
                    alert(typeof attrs.onChange);
                    attrs.onChange(scope.model == scope.onValue ? scope.offValue : scope.onValue);
                }
            };
        }
    };
});
