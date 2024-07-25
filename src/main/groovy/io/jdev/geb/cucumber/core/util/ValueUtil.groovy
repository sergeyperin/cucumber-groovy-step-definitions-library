/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 the original author or authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.jdev.geb.cucumber.core.util

import geb.content.Navigable
import geb.navigator.Navigator
import io.jdev.geb.cucumber.core.CheckedDecoder
import org.openqa.selenium.support.ui.Select

import java.util.regex.Pattern

class ValueUtil {

    static String getValue(def field) {
        if(field instanceof Navigator || field instanceof Navigable) {
            // gebish objects
            if((field instanceof Navigator && field.is('select')) || (field instanceof Navigable && field.$().is('select'))) {
                // get the text for the selected option, rather than its id value
                return new Select(field.firstElement()).firstSelectedOption.text.trim()
            } else {
                def value = field.value()
                if(!value) {
                    // maybe a plain div rather than input field
                    value = field.text()
                }
                return value?.trim()
            }
        } else {
            (field as String)?.trim()
        }
    }

    static void hasValue(def field, def expectedValueObj) {
        if(expectedValueObj instanceof CheckedDecoder.CheckedState) {
            boolean isChecked = field.value() != false
            boolean wantChecked = expectedValueObj == CheckedDecoder.CheckedState.checked
            assert isChecked == wantChecked
        } else {
            String fieldValue = getValue(field)
            // separate vars here just to give nicer assert messages on failure
            if(expectedValueObj instanceof Pattern) {
                Pattern expectedPattern = expectedValueObj
                assert fieldValue ==~ expectedPattern
            } else {
                // simple string equality
                String expectedValue = expectedValueObj as String
                assert fieldValue == expectedValue
            }
        }
    }

    static void enterValue(def field, def value) {
        if(value instanceof CheckedDecoder.CheckedState) {
            boolean isChecked = field.value() != false
            boolean wantChecked = value == CheckedDecoder.CheckedState.checked
            if(isChecked != wantChecked) {
                field.click()
            }
        } else {
            field.value(value)
        }
    }

}
